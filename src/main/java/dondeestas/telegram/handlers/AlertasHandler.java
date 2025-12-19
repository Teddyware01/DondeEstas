package dondeestas.telegram.handlers;

import dondeestas.service.MascotaService;
import dondeestas.entity.Mascota;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertasHandler extends BaseHandler {

    public final int DISTANCIA_MAX_KM = 399999990;
    private final MascotaService mascotaService;

    // Mapa para guardar el filtro elegido por cada chat
    private final Map<Long, String> filtrosPorChat = new HashMap<>();

    public AlertasHandler(MascotaService mascotaService, OkHttpTelegramClient telegramClient) {
        super(telegramClient);
        this.mascotaService = mascotaService;
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && (update.getMessage().getText().startsWith("/alertas")
                || update.getMessage().getText().equals("No enviar, mostrar todos")
                || update.getMessage().getText().equals("Enviar ubicación"))
                || (update.hasMessage() && update.getMessage().hasLocation());
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        if (chatId == null) return;

        // Caso: el mensaje trae ubicación
        if (update.hasMessage() && update.getMessage().hasLocation()) {
            Double lat = update.getMessage().getLocation().getLatitude();
            Double lon = update.getMessage().getLocation().getLongitude();

            String filtro = filtrosPorChat.getOrDefault(chatId, "todos");
            List<Mascota> mascotas = obtenerMascotasPorFiltro(filtro);
            mascotas = MascotaService.filtrarPorDistancia(mascotas, lat, lon, DISTANCIA_MAX_KM);

            String respuesta = formatearMascotas(mascotas, filtro);
            enviarTexto(chatId, respuesta);

            // Limpiar memoria del chat
            filtrosPorChat.remove(chatId);
            return;
        }

        // Caso: el usuario pulsa "No enviar, mostrar todos"
        if (update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().equals("No enviar, mostrar todos")) {

            String filtro = filtrosPorChat.getOrDefault(chatId, "todos");
            List<Mascota> mascotas = obtenerMascotasPorFiltro(filtro);

            String respuesta = formatearMascotas(mascotas, filtro);
            enviarTexto(chatId, respuesta);

            filtrosPorChat.remove(chatId);
            return;
        }

        // Caso: primer comando /alertas
        if (update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/alertas")) {

            String fullText = update.getMessage().getText();
            String filtro = extraerFiltro(fullText);

            // Guardar filtro en memoria por chatId
            filtrosPorChat.put(chatId, filtro);

            // Preguntar al usuario si desea enviar ubicación
            preguntarUbicacion(chatId);
        }
    }

    //Extrae el filtro del comando recibido
    private String extraerFiltro(String mensaje) {
        if (mensaje.contains(":propio")) return "propio";
        if (mensaje.contains(":ajeno")) return "ajeno";
        return "todos";
    }

     //Pregunta al usuario si desea enviar su ubicación
    private void preguntarUbicacion(Long chatId) {
        SendMessage message = new SendMessage(chatId.toString(), "¿Deseas enviar tu ubicación para ver solo mascotas cercanas?");

        KeyboardButton locationButton = new KeyboardButton("Enviar ubicación");
        locationButton.setRequestLocation(true);

        KeyboardButton todosButton = new KeyboardButton("No enviar, mostrar todos");

        KeyboardRow row = new KeyboardRow();
        row.add(locationButton);
        row.add(todosButton);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(List.of(row));
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);

        message.setReplyMarkup(keyboard);
        enviarMensaje(message);
    }

     //Obtiene la lista de mascotas según el filtro
    private List<Mascota> obtenerMascotasPorFiltro(String filtro) {
        return switch (filtro) {
            case "propio" -> mascotaService.listarMascotasPerdidasPropias();
            case "ajeno" -> mascotaService.listarMascotasPerdidasAjenas();
            default -> mascotaService.listarMascotasPerdidas();
        };
    }

 //Convierte la lista de mascotas en un texto legible para enviar al chat
    private String formatearMascotas(List<Mascota> mascotas, String filtro) {
        if (mascotas == null || mascotas.isEmpty()) {
            return "No hay mascotas reportadas en tu zona para el filtro '" + filtro + "'.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Mostrando mascotas (").append(filtro).append("):\n\n");
        for (Mascota m : mascotas) {
            sb.append("● Nombre: ").append(m.getNombre()).append("\n")
                    .append("ID: ").append(m.getId()).append("\n")
                    .append("Descripcion: ").append(m.getDescripcionExtra()).append("\n")
                    .append("Fecha publicacion: ").append(m.getFechaPerdida()).append("\n")
                    .append("Reportada por: ").append(m.getUsuario().getNombre()).append("\n")
                    .append("---\n");
        }

        return sb.toString();
    }
}

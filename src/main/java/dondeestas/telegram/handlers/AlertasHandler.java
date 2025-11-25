package dondeestas.telegram.handlers;

import dondeestas.service.MascotaService;
import dondeestas.entity.Mascota;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class AlertasHandler extends BaseHandler {
    public final int DISTANCIA_MAX_KM=30;
    private final MascotaService mascotaService;

    public AlertasHandler(MascotaService mascotaService, OkHttpTelegramClient telegramClient) {
        super(telegramClient);
        this.mascotaService = mascotaService;
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getText().startsWith("/alertas");
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        if (chatId == null) return;

        // Obtener el texto completo del comando
        String fullText = update.hasMessage() ? update.getMessage().getText()
                : update.getCallbackQuery().getData();

        // Extraer filtro (todos, propio, ajeno)
        String filtro = extraerFiltro(fullText);

        // Extraer ubicación si está presente
        Double lat = null, lon = null;
        if (update.hasMessage() && update.getMessage().hasLocation()) {
            lat = update.getMessage().getLocation().getLatitude();
            lon = update.getMessage().getLocation().getLongitude();
        }

        // Obtener la lista de mascotas según filtro y ubicación
        List<Mascota>  mascotas = mascotaService.listarCercanosConFiltro(lat, lon, DISTANCIA_MAX_KM, filtro);

        // Formatear respuesta y enviar al usuario
        String respuesta = formatearMascotas(mascotas, filtro);
        enviarTexto(chatId, respuesta);
    }

    /**
     * Extrae el filtro del comando recibido
     * @param mensaje texto completo del comando
     * @return "todos", "propio" o "ajeno"
     */
    private String extraerFiltro(String mensaje) {
        if (mensaje.contains(":propio")) return "propio";
        if (mensaje.contains(":ajeno")) return "ajeno";
        return "todos";
    }

    /**
     * Convierte la lista de mascotas en un texto legible para enviar al chat
     */
    private String formatearMascotas(List<Mascota> mascotas, String filtro) {
        if (mascotas == null || mascotas.isEmpty()) {
            return "No hay mascotas reportadas en tu zona para el filtro '" + filtro + "'.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Mostrando mascotas (").append(filtro).append("):\n\n");
        for (Mascota m : mascotas) {
            sb.append("Nombre: ").append(m.getNombre()).append("\n")
                    .append("Estado: ").append(m.getEstado()).append("\n")
                    .append("Fecha: ").append(m.getFecha()).append("\n");


            sb.append("Reportada por: ").append(m.getUsuario().getNombre()).append("\n")
                    .append("---\n");
        }

        return sb.toString();
    }
}

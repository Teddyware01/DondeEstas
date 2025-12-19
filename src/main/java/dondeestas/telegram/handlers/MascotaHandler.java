package dondeestas.telegram.handlers;

import dondeestas.entity.Mascota;
import dondeestas.service.MascotaService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class MascotaHandler extends BaseHandler {

    private final MascotaService mascotaService;

    public MascotaHandler(OkHttpTelegramClient telegramClient, MascotaService mascotaService) {
        super(telegramClient);
        this.mascotaService = mascotaService;
    }

    @Override
    public boolean canHandle(Update update) {
        // Manejamos mensajes que empiecen con /mascota
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().startsWith("/mascota");
        }
        // Opcional: si algún botón inline tiene callbackData "/mascota {id}"
        else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("/mascota")) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        if (chatId == null) return;

        String text = update.hasMessage() ? update.getMessage().getText()
                : update.getCallbackQuery().getData();

        // Extraer un ID de mascota si viene en el comando, por ejemplo "/mascota 123"
        String[] partes = text.split(" ");
        if (partes.length > 1) {
            String idMascota = partes[1];
            enviarTexto(chatId, "Mostrando ficha de la mascota con ID: " + idMascota);
            String mensaje = mascotaToTelegramMessage(mascotaService.buscarPorId(Long.valueOf(idMascota)));
            enviarTexto(chatId,mensaje);



        } else {
            enviarTexto(chatId, "Esta es la sección de mascota. Usa /mascota {id}, indicando el identificador de la mascota para ver sus detalles.");
        }
    }


    public static String mascotaToTelegramMessage(Optional<Mascota> mascotaOpt) {
        if (mascotaOpt.isEmpty()) return "Información de mascota no disponible.";
        Mascota mascota = mascotaOpt.get();
        StringBuilder sb = new StringBuilder();
        sb.append("🐾 *").append(mascota.getNombre()).append("*\n");
        sb.append("Tipo: ").append(mascota.getTipoAnimal()).append("\n");
        if (mascota.getTamano() != null) sb.append("Tamaño: ").append(mascota.getTamano()).append("\n");
        if (mascota.getColor() != null) sb.append("Color: ").append(mascota.getColor()).append("\n");
        sb.append("Estado: ").append(mascota.getEstado()).append("\n");
        if (mascota.getFecha() != null) sb.append("Fecha: ").append(mascota.getFecha()).append("\n");

        // Ubicación aproximada
        if (mascota.getDepartamento() != null || mascota.getProvincia() != null || mascota.getMunicipio() != null) {
            sb.append("📍 Ubicación: ");
            if (mascota.getMunicipio() != null) sb.append(mascota.getMunicipio());
            if (mascota.getDepartamento() != null) sb.append(mascota.getDepartamento() != null ? ", " + mascota.getDepartamento() : "");
            if (mascota.getProvincia() != null) sb.append(mascota.getProvincia() != null ? ", " + mascota.getProvincia() : "");
            sb.append("\n");
        }

        if (mascota.getDescripcionExtra() != null && !mascota.getDescripcionExtra().isEmpty()) {
            sb.append("ℹ️ ").append(mascota.getDescripcionExtra()).append("\n");
        }

        sb.append("Activo: ").append(mascota.isActivo() ? "Sí ✅" : "No ❌");
        return sb.toString();
    }
}

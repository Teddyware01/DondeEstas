package dondeestas.telegram.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MascotaHandler extends BaseHandler {

    public MascotaHandler(OkHttpTelegramClient telegramClient) {
        super(telegramClient);
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
        } else {
            enviarTexto(chatId, "Esta es la sección de mascota. Usa /mascota {id}, indicando el identificador de la mascota para ver sus detalles.");
        }
    }
}

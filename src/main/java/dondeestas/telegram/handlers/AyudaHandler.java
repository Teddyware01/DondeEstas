package dondeestas.telegram.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class AyudaHandler extends BaseHandler {

    public AyudaHandler(OkHttpTelegramClient telegramClient) {
        super(telegramClient);
    }

    @Override
    public boolean canHandle(Update update) {
        // Manejamos mensajes que empiecen con /ayuda o callbackData igual a "/ayuda"
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().startsWith("/ayuda");
        } else if (update.hasCallbackQuery() && "/ayuda".equals(update.getCallbackQuery().getData())) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update); // ya viene del BaseHandler
        if (chatId == null) return;

        String texto = "🤖 Ayuda del bot\n\n" +
                "• /alertas -> ver mascotas perdidas en tu zona\n" +
                "• /alertas:propio -> sólo reportadas por dueños\n" +
                "• /alertas:ajeno -> sólo reportadas por terceros\n" +
                "• /mascota {id} -> ver ficha de la mascota\n" +
                "• /ranking -> ranking de voluntarios";

        enviarTexto(chatId, texto);
    }
}

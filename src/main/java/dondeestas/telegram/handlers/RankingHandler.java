package dondeestas.telegram.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RankingHandler extends BaseHandler {

    public RankingHandler(OkHttpTelegramClient telegramClient) {
        super(telegramClient);
    }

    @Override
    public boolean canHandle(Update update) {
        // Manejamos mensajes que empiecen con /ranking
        if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText().startsWith("/ranking");
        }
        // Opcional: si algún botón inline tiene callbackData "/ranking"
        else if (update.hasCallbackQuery() && "/ranking".equals(update.getCallbackQuery().getData())) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        if (chatId == null) return;

        // Aquí puedes llamar a un servicio que calcule el ranking
        enviarTexto(chatId, "Mostrando ranking de voluntarios...");
    }
}

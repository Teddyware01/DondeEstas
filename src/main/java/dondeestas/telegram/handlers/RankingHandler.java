package dondeestas.telegram.handlers;

import dondeestas.auxClass.RankingItem;
import dondeestas.service.RankingService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class RankingHandler extends BaseHandler {

    private final RankingService rankingService;

    public RankingHandler(OkHttpTelegramClient telegramClient, RankingService rankingService) {
        super(telegramClient);
        this.rankingService = rankingService;
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

        List<RankingItem> ranking = rankingService.obtenerRankingLimit(30);

        StringBuilder sb = new StringBuilder();
        sb.append("🏆 *Ranking de Usuarios*\n\n");

        int pos = 1;
        for (RankingItem item : ranking) {
            sb.append(item.toRankingString(pos));
            sb.append("\n");
            pos++;
        }

        enviarTexto(chatId, sb.toString());
    }

}

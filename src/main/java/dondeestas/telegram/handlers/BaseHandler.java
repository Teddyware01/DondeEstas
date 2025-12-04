package dondeestas.telegram.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class BaseHandler implements BotHandler {

    protected final OkHttpTelegramClient telegramClient;

    public BaseHandler(OkHttpTelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    protected void enviarTexto(Long chatId, String texto) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(texto)
                .build();

        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    protected void enviarMensaje(SendMessage message) {
        try {
            // execute() es del TelegramLongPollingBot o del cliente que estés usando
            telegramClient.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Long getChatId(Update update) {
        if (update.hasMessage()) return update.getMessage().getChatId();
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return null;
    }
}

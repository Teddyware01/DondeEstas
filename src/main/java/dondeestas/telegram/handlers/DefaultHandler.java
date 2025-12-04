package dondeestas.telegram.handlers;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultHandler extends BaseHandler {

    public DefaultHandler(OkHttpTelegramClient telegramClient) {
        super(telegramClient);
    }

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public void handle(Update update) {
        Long chatId = null;

        // Si es mensaje normal
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        }
        // Si es CallbackQuery (botón inline)
        else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        if (chatId == null) {
            System.out.println("Update no tiene chatId válido: " + update);
            return; // no podemos responder
        }

        String texto = "No entendí tu mensaje 😅\n" +
                "Si necesitas ayuda, pulsa el siguiente botón:";

        // Crear botón inline
        InlineKeyboardButton botonAyuda = InlineKeyboardButton.builder()
                .text("Ayuda")
                .callbackData("/ayuda") // será recibido como CallbackQuery
                .build();

        // Crear fila y agregar el botón
        InlineKeyboardRow fila = new InlineKeyboardRow();
        fila.add(botonAyuda);

        // Crear lista de filas
        List<InlineKeyboardRow> teclado = new ArrayList<>();
        teclado.add(fila);

        // Crear markup con las filas
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(teclado);

        // Enviar mensaje con el botón
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(texto)
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

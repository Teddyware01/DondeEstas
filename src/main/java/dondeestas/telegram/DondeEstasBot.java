package dondeestas.telegram;

import dondeestas.entity.Mascota;
import dondeestas.repository.MascotaRepository;
import dondeestas.service.MascotaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

@Component
public class DondeEstasBot implements LongPollingSingleThreadUpdateConsumer {
    private final MascotaService mascotaService;
    private String botToken;
    private TelegramClient telegramClient;
    private DispatcherService dispatcher;

    public DondeEstasBot(@Value("${telegram.token}") String botToken, MascotaService mascotaService, DispatcherService dispatcher) {
        super();
        this.botToken = botToken;
        this.dispatcher=dispatcher;
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.mascotaService = mascotaService;
    }
    @Override
    public void consume(Update update) {
        dispatcher.dispatch(update);
    }


}
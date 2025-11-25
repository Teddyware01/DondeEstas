package dondeestas.telegram;

import dondeestas.telegram.handlers.BotHandler;
import dondeestas.telegram.handlers.DefaultHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

@Service
public class DispatcherService {

    private final List<BotHandler> handlers;

    public DispatcherService(List<BotHandler> handlers) {
        this.handlers = handlers;
    }

    public void dispatch(Update update) {
        // Primero intentamos con los handlers que revisan mensajes
        if (update.hasMessage() && update.getMessage().hasText()) {
            for (BotHandler handler : handlers) {
                if (handler.canHandle(update) && !(handler instanceof DefaultHandler)) {
                    handler.handle(update);
                    return;
                }
            }
        }

        // Ahora manejamos CallbackQuery
        else if (update.hasCallbackQuery()) {
            for (BotHandler handler : handlers) {
                if (handler.canHandle(update)) { // handler revisa callbackData dentro de canHandle
                    handler.handle(update);
                    return;
                }
            }
        }

        // DefaultHandler si nada más coincide
        handlers.stream()
                .filter(h -> h instanceof DefaultHandler)
                .findFirst()
                .ifPresent(h -> h.handle(update));
    }




}

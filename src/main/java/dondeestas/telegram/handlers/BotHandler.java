package dondeestas.telegram.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotHandler {
    boolean canHandle(Update update); // devuelve true si este handler debe procesar el update
    void handle(Update update);
}

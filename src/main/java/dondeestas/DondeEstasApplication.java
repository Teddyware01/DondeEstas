package dondeestas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import dondeestas.telegram.DondeEstasBot;

@SpringBootApplication
public class DondeEstasApplication {

    public static void main(String[] args) throws Exception {
        var context = SpringApplication.run(DondeEstasApplication.class, args);

        // ⚠️ OBTENER BOT DESDE SPRING
        DondeEstasBot bot = context.getBean(DondeEstasBot.class);

        // ⚠️ Y OBTENER EL TOKEN DESDE APPLICATION.PROPERTIES
        String botToken = context.getEnvironment().getProperty("telegram.token");

        try (TelegramBotsLongPollingApplication botsApp = new TelegramBotsLongPollingApplication()) {

            botsApp.registerBot(botToken, bot);

            System.out.println("Bot iniciado correctamente!");

            Thread.currentThread().join();
        }
    }
}

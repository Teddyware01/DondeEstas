package dondeestas;

import dondeestas.service.MascotaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import telegram.DondeEstasBot;

import java.util.Properties;

@SpringBootApplication
public class DondeEstasApplication {
    public static void main(String[] args) {
        SpringApplication.run(DondeEstasApplication.class, args);

        String botToken="8480878618:AAGyi3cGUpz6XuGUz18YlNtvgNvj9444jO4";

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new DondeEstasBot(botToken,));
            System.out.println("Bot iniciado correctamente!");
            // Ensure this prcess wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package dondeestas.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Configuration
public class TelegramConfig {

    @Value("${telegram.token}")
    private String botToken;

    @Bean
    public OkHttpTelegramClient telegramClient() {
        return new OkHttpTelegramClient(botToken);
    }
}

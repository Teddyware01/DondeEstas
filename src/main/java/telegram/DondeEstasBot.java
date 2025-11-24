package telegram;

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

    public DondeEstasBot(@Value("${telegram.token}") String botToken, MascotaService mascotaService) {
        super();
        this.botToken = botToken;
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.mascotaService = mascotaService;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        try {
            procesarMensaje(update, messageText, chatId);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void procesarMensaje(Update update, String messageText, long chatId) throws TelegramApiException {

        switch (messageText) {
            case "/alertas":
                manejarAlertas(update);
                break;

            case "/mascota":
                manejarMascota(update);
                break;

            case "/ranking":
                manejarRanking(update);
                break;

            case "/ayuda":
                manejarAyuda(update);
                break;

            default:
                enviarSaludoConTeclado(chatId);
                break;
        }
    }

    private void enviarSaludoConTeclado(long chatId) {

        KeyboardRow row1 = new KeyboardRow();
        row1.add("/start");
        row1.add("/alertas");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("/mascota");
        row2.add("/ranking");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("/ayuda");

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .keyboardRow(row3)
                .resizeKeyboard(true)
                .selective(true)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("¡Hola! Seleccioná una opción del menú:")
                .replyMarkup(keyboardMarkup)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void manejarAlertas(Update update) {
        String fullText=update.getMessage().getText();
        long chatId=update.getMessage().getChatId();
        // Determinar filtro propio|ajeno|ninguno
        String filtro = "todos";
        if (fullText.contains(":propio")) filtro = "propio";
        else if (fullText.contains(":ajeno")) filtro = "ajeno";
        String respuesta="";
        if (filtro == "todos"){
            respuesta= "Mostrando todas las mascotas para tu zona\n" +
            mascotaService.listarTodas().toString();
        } else if (filtro=="propio") {
            respuesta="Mostrando todas las mascotas publicadas en busqueda por sus dueños, para tu zona\n:"+
            //mascotaService. FALTA AGREGAR EL METODO, CUANDO IMPLEMENTEMOS LOS ESTADOS SE PODRA,
                    "";
        } else  if (filtro=="ajeno") {
            respuesta="Mostrando todas las mascotas ajenas, buscando a su dueño, en tu zona \n"+
            //mascotaService. FALTA AGREGAR EL METODO, CUANDO IMPLEMENTEMOS LOS ESTADOS SE PODRA,
            "";

        }
        enviarTexto(chatId, respuesta);
    }

    private void manejarMascota(Update update) {
        long chatId = update.getMessage().getChatId();
        enviarTexto(chatId, "Esta es la sección de mascota");
    }

    private void manejarRanking(Update update) {
        long chatId = update.getMessage().getChatId();

        enviarTexto(chatId, "Mostrando ranking");
    }

    private void manejarAyuda(Update update) throws TelegramApiException {
        // Texto simple (no MarkdownV2) + teclado con botones para insertar comandos
        long chatId = update.getMessage().getChatId();
        String texto =
                "🤖 Ayuda del bot\n\n" +
                        "• /alertas -> ver mascotas perdidas en tu zona\n" +
                        "• /alertas:propio -> sólo reportadas por dueños\n" +
                        "• /alertas:ajeno -> sólo reportadas por terceros\n" +
                        "• /mascota {id} -> ver ficha de la mascota\n" +
                        "• /ranking -> ranking de voluntarios";

        // Reutilizamos el mismo teclado o uno simplificado
        KeyboardRow r1 = new KeyboardRow();
        r1.add(new KeyboardButton("/alertas:propio"));
        r1.add(new KeyboardButton("/alertas:ajeno"));

        KeyboardRow r2 = new KeyboardRow();
        r2.add(new KeyboardButton("/mascota 123"));
        r2.add(new KeyboardButton("/ranking"));

        List<KeyboardRow> kb = new ArrayList<>();
        kb.add(r1);
        kb.add(r2);

        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .keyboard(kb)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(texto)
                .replyMarkup(keyboard)
                .build();

        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void enviarTexto(long chatId, String texto) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(texto)
                .parseMode("MarkdownV2")
                .build();

        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
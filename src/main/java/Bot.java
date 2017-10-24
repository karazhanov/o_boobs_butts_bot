import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * @author karazhanov on 24.10.17.
 */
public class Bot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new OBBBot());
            System.out.println("Bot started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

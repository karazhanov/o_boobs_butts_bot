import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author karazhanov on 24.10.17.
 */
public class OBBBot extends TelegramLongPollingBot {

    private ImageChoiser imageChoiser;
    private ReplyKeyboardMarkup keyboardMarkup;

    public OBBBot() {
        imageChoiser = new ImageChoiser();

        keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(new KeyboardButton(CONST.BOOBS_COMMAND));
        keyboardButtons.add(new KeyboardButton(CONST.BUTTS_COMMAND));
        keyboard.add(keyboardButtons);
        keyboardMarkup.setKeyboard(keyboard);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                parceMessage(message);
            }
        }
    }

    private void parceMessage(Message message) {
        try {
            if (message.isCommand()) {
                executeCommand(message);
            } else {
                executeText(message);
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    private void executeText(Message message) throws TelegramApiException {
        System.out.println("TEXT " + message.getText() + " from " + message.getFrom());
        sendText(message, "You can send only commands:\n/butts\n/boobs");
    }

    private void executeCommand(Message message) throws TelegramApiException, IOException {
        System.out.println("COMMAND " + message.getText() + " from " + message.getFrom());
        makeResponse(message);
    }

    private void makeResponse(Message message) throws TelegramApiException, IOException {
        String cmd = message.getText();
        if(CONST.START_COMMAND.equals(cmd)) {
            sendText(message, CONST.START_INFO_DESCRIPTION);
            return;
        }
        if(CONST.BOOBS_COMMAND.equals(cmd)) {
            sendPhoto(message, imageChoiser.getRandomImage(ImageType.BOOBS));
            return;
        }
        if(CONST.BUTTS_COMMAND.equals(cmd)) {
            sendPhoto(message, imageChoiser.getRandomImage(ImageType.BUTTS));
            return;
        }
        sendText(message, cmd + " is invalid. Choose valid command");
    }



    private void sendText(Message message, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(message.getChatId())
                .setText(text)
                .setReplyMarkup(keyboardMarkup);
        sendMessage(sendMessage);
    }

    private void sendPhoto(Message message, String photoUrl) throws TelegramApiException {
        SendPhoto photo = new SendPhoto();
        photo
                .setChatId(message.getChatId())
                .setPhoto(photoUrl)
                .setReplyMarkup(keyboardMarkup);
        sendPhoto(photo);
    }

    @Override
    public String getBotUsername() {
        return CONST.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return CONST.BOT_TOKEN;
    }
}

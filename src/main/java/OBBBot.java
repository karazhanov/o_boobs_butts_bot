import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author karazhanov on 24.10.17.
 */
public class OBBBot extends TelegramLongPollingBot {

    private ImageChoiser imageChoiser;

    public OBBBot() {
        imageChoiser = new ImageChoiser();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                parceMessage(message);
            }
        }
    }

    private void parceMessage(Message message) {
        SendMessage messageResponse;
        if (message.isCommand()) {
            messageResponse = executeCommand(message);
        } else {
            messageResponse = executeText(message);
        }
        try {
            sendMessage(messageResponse); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage executeText(Message message) {
        System.out.println("TEXT " + message.getText() + " from " + message.getFrom());
        return new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(message.getChatId())
                .setText("You can send only commands:\n/butts\n/boobs")
                .setReplyMarkup(getKeyboard());
    }

    private SendMessage executeCommand(Message message) {
        System.out.println("COMMAND " + message.getText() + " from " + message.getFrom());
        String response = getResponse(message);
        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(response)
                .setReplyMarkup(getKeyboard());
    }

    private String getResponse(Message message) {
        try {
            switch (message.getText()) {
                case CONST.BOOBS_COMMAND: return imageChoiser.getRandomImage(ImageType.BOOBS);
                case CONST.BUTTS_COMMAND: return imageChoiser.getRandomImage(ImageType.BUTTS);
                case CONST.START_COMMAND: return CONST.START_INFO_DESCRIPTION;
                default: return "Choose valid command";
            }
        } catch (IOException | TelegramApiValidationException e) {
            return "ERROR";
        }
    }

    private ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(new KeyboardButton(CONST.BOOBS_COMMAND));
        keyboardButtons.add(new KeyboardButton(CONST.BUTTS_COMMAND));
        keyboard.add(keyboardButtons);
        keyboardMarkup.setKeyboard(keyboard);
        return  keyboardMarkup;
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

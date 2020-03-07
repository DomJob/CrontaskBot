package telegram;

import application.TelegramAPI;
import java.util.List;

public class TelegramHttpAPI implements TelegramAPI {
    // TODO

    @Override
    public List<Update> getUpdates(long offset) {
        return null;
    }

    @Override
    public void sendMessage(long chatId, String text) {

    }

    @Override
    public void sendMessageWithKeyboard(long chatId, String text, List<String> buttons) {

    }

    @Override
    public void answerCallbackQuery(String id, String text) {

    }
}

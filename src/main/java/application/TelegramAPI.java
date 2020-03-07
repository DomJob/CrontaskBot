package application;

import java.util.List;
import telegram.Update;

public interface TelegramAPI {
    List<Update> getUpdates(long offset);

    void sendMessage(long chatId, String text);

    void sendMessageWithKeyboard(long chatId, String text, List<String> buttons);

    void answerCallbackQuery(String id, String text);
}

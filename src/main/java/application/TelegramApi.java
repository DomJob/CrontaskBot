package application;

import application.entities.Button;
import application.entities.Update;
import java.util.List;

public interface TelegramApi {
    List<Update> getUpdates(long offset);

    void sendMessage(long chatId, String text);

    void sendMessageWithKeyboard(long chatId, String text, List<Button> buttons);

    void answerCallbackQuery(String id, String text);
}

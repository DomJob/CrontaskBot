package application;

import application.entities.Button;
import java.util.List;

public interface TelegramApi {
    void sendMessage(long chatId, String text);

    void sendMessageWithKeyboard(long chatId, String text, List<Button> buttons);

    void answerCallbackQuery(String id, String text);
}

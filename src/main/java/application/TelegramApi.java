package application;

import application.entities.Update;
import java.util.List;

public interface TelegramApi {
    List<Update> getUpdates(long offset);

    void sendMessage(Message message);

    void answerCallbackQuery(String id, String text);
}

package application;

import application.entities.Update;
import application.message.Message;
import java.util.List;

public interface TelegramApi {
    List<Update> getUpdates(long offset);

    void sendMessage(Message message);

    void answerCallbackQuery(String id, String text);

    void deleteMessage(long chatId, long messageId);
}

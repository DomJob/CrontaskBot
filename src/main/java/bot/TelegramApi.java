package bot;

import bot.entities.Update;
import bot.message.Message;
import java.util.List;

public interface TelegramApi {
    List<Update> getUpdates(long offset);

    void sendMessage(Message message);

    void answerCallbackQuery(String id, String text);

    void deleteMessage(long chatId, long messageId);
}

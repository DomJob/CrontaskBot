package bot;

import bot.message.Message;
import bot.models.Update;
import domain.user.UserId;
import java.util.List;

public interface TelegramApi {
    List<Update> getUpdates(long offset);

    void sendMessage(Message message);

    void answerCallbackQuery(String id, String text);

    void deleteMessage(UserId chatId, long messageId);
}

package bot.entities;

import domain.time.Time;
import domain.user.UserId;

public class CallbackQuery {
    public String id;
    public UserId userId;
    public long messageId;
    public String data;
    public Time time;

    public CallbackQuery(String id, UserId userId, long messageId, String data) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.data = data;
    }
}

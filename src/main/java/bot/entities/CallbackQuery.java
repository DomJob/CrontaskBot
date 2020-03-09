package bot.entities;

import domain.time.Time;

public class CallbackQuery {
    public String id;
    public long userId;
    public long messageId;
    public String data;
    public Time time;

    public CallbackQuery(String id, long userId, long messageId, String data) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.data = data;
    }
}

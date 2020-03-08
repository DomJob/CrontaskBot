package application.entities;

public class CallbackQuery {
    public String id;
    public long userId;
    public long messageId;
    public String data;

    public CallbackQuery(String id, long userId, long messageId, String data) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.data = data;
    }
}

package application.entities;

public class CallbackQuery {
    public String id;
    public long sender;
    public long messageId;
    public String data;

    public CallbackQuery(String id, long sender, long messageId, String data) {
        this.id = id;
        this.sender = sender;
        this.messageId = messageId;
        this.data = data;
    }
}

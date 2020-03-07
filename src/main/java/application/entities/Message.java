package application.entities;

public class Message {
    public long sender;
    public String text;

    public Message(long sender, String text) {
        this.sender = sender;
        this.text = text;
    }
}

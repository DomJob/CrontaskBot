package bot.message;

import bot.entities.Button;
import domain.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {
    private User receiver;
    private String text;
    private List<Button> buttons = new ArrayList<>();

    public Message(String text) {
        this.text = text;
    }

    public void addButton(String text, String data) {
        buttons.add(new Button(text, data));
    }

    public List<Button> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public long getReceiverId() {
        return receiver.getId();
    }

    public String getText() {
        return text;
    }
}

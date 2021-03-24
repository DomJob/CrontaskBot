package bot.message;

import bot.models.Button;
import domain.user.User;
import domain.user.UserId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {
    private User receiver;
    private final String text;
    private final List<Button> buttons = new ArrayList<>();

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, User receiver) {
        this.text = text;
        this.receiver = receiver;
    }

    public void addButton(String text, String data) {
        buttons.add(new Button(text, data));
    }

    public List<Button> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public UserId getReceiverId() {
        return receiver.getId();
    }

    public String getText() {
        return text;
    }
}

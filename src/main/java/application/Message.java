package application;

import application.entities.Button;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {
    private long receiver;
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

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }

    public long getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }
}

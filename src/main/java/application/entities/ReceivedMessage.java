package application.entities;

import application.command.Command;
import domain.time.Time;

public class ReceivedMessage {
    public long userId;
    public String text;
    public Time time;

    public ReceivedMessage(long userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public Command getCommand() {
        return Command.parse(text);
    }
}

package application.entities;

import application.command.Command;

public class ReceivedMessage {
    public long userId;
    public String text;

    public ReceivedMessage(long userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public Command getCommand() {
        return Command.parse(text);
    }
}

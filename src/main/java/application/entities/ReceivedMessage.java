package application.entities;

import application.command.Command;

public class ReceivedMessage {
    public long user;
    public String text;

    public ReceivedMessage(long user, String text) {
        this.user = user;
        this.text = text;
    }

    public Command getCommand() {
        return Command.parse(text);
    }
}

package application.entities;

import application.command.Command;

public class Message {
    public long sender;
    public String text;

    public Message(long sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public Command getCommand() {
        return Command.parse(text);
    }
}

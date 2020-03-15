package bot.models;

import bot.command.Command;
import domain.time.Time;
import domain.user.UserId;

public class ReceivedMessage {
    public UserId userId;
    public String text;
    public Time time;

    public ReceivedMessage(UserId userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public Command getCommand() {
        return Command.parse(text);
    }
}

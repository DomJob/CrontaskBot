package bot.states;

import bot.command.Command;
import bot.entities.ReceivedMessage;
import domain.time.Timezone;

public class TimezoneOffsetRequestedState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        if(message.getCommand() == Command.CANCEL) {
            context.sendOperationCancelledMessage();
            return new DefaultState();
        }

        try {
            Timezone timezone = Timezone.fromString(message.text);

            context.setTimezone(timezone);
            context.sendTimezoneSetMessage();

            return new DefaultState();
        } catch (IllegalArgumentException e) {
            context.sendInvalidTimezoneMessage();
            return this;
        }
    }
}

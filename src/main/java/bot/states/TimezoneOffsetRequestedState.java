package bot.states;

import bot.entities.ReceivedMessage;
import domain.time.Timezone;

public class TimezoneOffsetRequestedState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        Timezone timezone = Timezone.fromString(message.text);

        context.setTimezone(timezone);

        return new DefaultState();
    }
}

package bot.states;

import bot.models.ReceivedMessage;

public interface BotState {
    BotState handleMessage(ReceivedMessage message, BotContext context);
}

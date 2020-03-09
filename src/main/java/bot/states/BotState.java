package bot.states;

import bot.entities.ReceivedMessage;

public interface BotState {
    BotState handleMessage(ReceivedMessage message, BotContext context);
}

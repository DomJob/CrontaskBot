package application.states;

import application.entities.ReceivedMessage;

public interface BotState {
    BotState handleMessage(ReceivedMessage message, BotContext context);
}

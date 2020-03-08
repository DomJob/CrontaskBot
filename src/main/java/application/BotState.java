package application;

import application.entities.ReceivedMessage;
import application.states.BotContext;

public interface BotState {
    BotState handleMessage(ReceivedMessage message, BotContext context);
}

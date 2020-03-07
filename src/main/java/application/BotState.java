package application;

import application.entities.Message;

public interface BotState {
    BotState handleMessage(Message message, CrontaskBot bot);
}

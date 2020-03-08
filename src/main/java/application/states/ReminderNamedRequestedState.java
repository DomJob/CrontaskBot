package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.Messages;

public class ReminderNamedRequestedState implements BotState {
    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        bot.sendMessage(message.sender, Messages.reminderTimeRequestedMessage());
        return new ReminderTimeRequestedState(message.text);
    }
}
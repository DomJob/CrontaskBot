package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.Messages;

public class DefaultState implements BotState {
    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        switch (message.getCommand()) {
            case START:
                bot.sendMessage(message.sender, Messages.startMessage());
                return this;
            case NEW_TASK:
                bot.sendMessage(message.sender, Messages.taskNameRequestedMessage());
                return new TaskNameRequestedState();
            case NEW_REMINDER:
                bot.sendMessage(message.sender, Messages.reminderNameRequestedMessage());
                return new ReminderNamedRequestedState();
            case HELP:
                bot.sendMessage(message.sender, Messages.helpMessage());
                return this;
            case CANCEL:
                bot.sendMessage(message.sender, Messages.noOngoingOperation());
                return this;
            case UNKNOWN:
                bot.sendMessage(message.sender, Messages.unknownCommand());
                return this;
            default:
                bot.sendMessage(message.sender, Messages.defaultMessage());
                return this;
        }
    }
}
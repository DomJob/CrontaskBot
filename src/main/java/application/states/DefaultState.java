package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.MessageFormatter;

public class DefaultState implements BotState {
    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        bot.sendMessage(message.sender, MessageFormatter.getNameRequestMessage());

        return new NameRequestedState();
    }
}
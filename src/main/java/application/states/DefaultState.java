package application.states;

import application.BotState;
import application.CrontaskBot;
import application.command.Command;
import application.command.NotACommandException;
import application.command.UnknownCommandException;
import application.entities.Message;
import configuration.MessageFormatter;

public class DefaultState implements BotState {
    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        Command command;

        try {
            command = Command.parse(message.text);
        } catch (NotACommandException | UnknownCommandException e) {
            bot.sendMessage(message.sender, MessageFormatter.getDefaultMessage());
            return this;
        }

        switch (command) {
            case START:
                bot.sendMessage(message.sender, MessageFormatter.getStartMessage());
                return this;
            case NEWTASK:
                bot.sendMessage(message.sender, MessageFormatter.getNameRequestMessage());
                return new NameRequestedState();
            case HELP:
                bot.sendMessage(message.sender, MessageFormatter.getHelpMessage());
                return this;
            default:
                bot.sendMessage(message.sender, MessageFormatter.getDefaultMessage());
                return this;
        }
    }
}
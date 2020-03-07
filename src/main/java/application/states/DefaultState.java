package application.states;

import application.BotState;
import application.CrontaskBot;
import application.command.Command;
import application.command.NotACommandException;
import application.command.UnknownCommandException;
import application.entities.Message;
import configuration.Messages;

public class DefaultState implements BotState {
    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        Command command;

        try {
            command = Command.parse(message.text);
        } catch (NotACommandException e) {
            bot.sendMessage(message.sender, Messages.defaultMessage());
            return this;
        } catch (UnknownCommandException e) {
            bot.sendMessage(message.sender, Messages.unknownCommand());
            return this;
        }

        switch (command) {
            case START:
                bot.sendMessage(message.sender, Messages.startMessage());
                return this;
            case NEWTASK:
                bot.sendMessage(message.sender, Messages.nameRequestedMessage());
                return new NameRequestedState();
            case HELP:
                bot.sendMessage(message.sender, Messages.helpMessage());
                return this;
            default:
                bot.sendMessage(message.sender, Messages.defaultMessage());
                return this;
        }
    }
}
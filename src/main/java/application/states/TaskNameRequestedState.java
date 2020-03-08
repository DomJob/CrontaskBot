package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.Messages;

public class TaskNameRequestedState implements BotState {
    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        bot.sendMessage(message.sender, Messages.cronRequestedMessage());
        return new CronScheduleRequestedState(message.text);
    }
}

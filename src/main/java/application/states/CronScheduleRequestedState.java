package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.Messages;
import domain.Schedule;
import domain.cronschedule.CronSchedule;
import domain.cronschedule.InvalidCronFormatException;

public class CronScheduleRequestedState implements BotState {
    private String taskName;

    public CronScheduleRequestedState(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        try {
            Schedule schedule = CronSchedule.parse(message.text);

            bot.createTask(taskName, message.sender, schedule);
            bot.sendMessage(message.sender, Messages.taskCreatedMessage());

            return new DefaultState();
        } catch (InvalidCronFormatException e) {
            bot.sendMessage(message.sender, Messages.invalidCronMessage());
            return this;
        }
    }
}

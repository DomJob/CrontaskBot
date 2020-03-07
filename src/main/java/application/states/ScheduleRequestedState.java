package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.Messages;
import domain.Schedule;
import domain.cronschedule.CronSchedule;

public class ScheduleRequestedState implements BotState {
    private String taskName;

    public ScheduleRequestedState(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        bot.sendMessage(message.sender, Messages.taskCreatedMessage());

        Schedule schedule = CronSchedule.parse(message.text);

        bot.createTask(taskName, message.sender, schedule);

        return new DefaultState();
    }
}

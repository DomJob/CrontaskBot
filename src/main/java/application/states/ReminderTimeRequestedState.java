package application.states;

import application.BotState;
import application.CrontaskBot;
import application.entities.Message;
import configuration.Messages;
import domain.reminderschedule.ReminderSchedule;

public class ReminderTimeRequestedState implements BotState {
    private String reminderName;

    public ReminderTimeRequestedState(String reminderName) {
        this.reminderName = reminderName;
    }

    @Override
    public BotState handleMessage(Message message, CrontaskBot bot) {
        // TODO - Parse reminder time

        ReminderSchedule schedule = new ReminderSchedule(null);

        bot.createReminder(reminderName, message.sender, schedule);
        bot.sendMessage(message.sender, Messages.taskCreatedMessage());

        return new DefaultState();
    }
}

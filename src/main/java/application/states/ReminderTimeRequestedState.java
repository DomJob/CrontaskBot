package application.states;

import application.BotState;
import application.command.Command;
import application.entities.ReceivedMessage;
import domain.reminderschedule.InvalidReminderFormatException;
import domain.reminderschedule.ReminderSchedule;

class ReminderTimeRequestedState implements BotState {
    private String reminderName;

    public ReminderTimeRequestedState(String reminderName) {
        this.reminderName = reminderName;
    }

    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        if(message.getCommand() == Command.CANCEL) {
            context.sendOperationCancelledMessage();

            return new DefaultState();
        }

        try {
            ReminderSchedule schedule = ReminderSchedule.parse(message.text);
            context.createReminder(reminderName, schedule);
            context.sendReminderCreatedMessage();

            return new DefaultState();
        } catch (InvalidReminderFormatException e) {
            context.sendInvalidReminderScheduleMessage();

            return this;
        }

    }
}

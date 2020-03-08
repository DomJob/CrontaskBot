package application.states;

import application.command.Command;
import application.entities.ReceivedMessage;
import domain.Schedule;
import domain.cronschedule.CronSchedule;
import domain.cronschedule.InvalidCronFormatException;

class CronScheduleRequestedState implements BotState {
    private String taskName;

    public CronScheduleRequestedState(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        if (message.getCommand() == Command.CANCEL) {
            context.sendOperationCancelledMessage();
            return new DefaultState();
        }

        try {
            Schedule schedule = CronSchedule.parse(message.text);

            context.createTask(taskName, schedule);
            context.sendTaskCreatedMessage();

            return new DefaultState();
        } catch (InvalidCronFormatException e) {
            context.sendInvalidCronFormatMessage();
            return this;
        }
    }
}

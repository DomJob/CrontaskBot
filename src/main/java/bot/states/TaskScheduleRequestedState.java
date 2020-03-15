package bot.states;

import bot.command.Command;
import bot.models.ReceivedMessage;
import domain.schedule.InvalidScheduleException;
import domain.schedule.Schedule;
import domain.time.Time;

class TaskScheduleRequestedState implements BotState {
    private String taskName;

    public TaskScheduleRequestedState(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        if (message.getCommand() == Command.CANCEL) {
            context.sendOperationCancelledMessage();
            return new DefaultState();
        }

        try {
            Time timeInTimezone = message.time.withTimezone(context.getTimezone());
            Schedule schedule = Schedule.parse(message.text, timeInTimezone);
            context.createTask(taskName, schedule);
        } catch (InvalidScheduleException e) {
            context.sendInvalidScheduleFormat();
            return this;
        }

        context.sendTaskCreatedMessage();
        return new DefaultState();
    }
}

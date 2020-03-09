package application.states;

import application.command.Command;
import application.entities.ReceivedMessage;
import domain.schedule.InvalidScheduleException;
import domain.schedule.Schedule;
import domain.schedule.ScheduleParser;

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
            Schedule schedule = new ScheduleParser().parse(message.text, message.time);
            context.createTask(taskName, schedule);
        } catch (InvalidScheduleException e) {
            context.sendInvalidScheduleFormat();
            return this;
        }

        context.sendTaskCreatedMessage();
        return new DefaultState();
    }
}

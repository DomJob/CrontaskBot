package application.states;

import application.BotState;
import application.command.Command;
import application.entities.ReceivedMessage;

class TaskNameRequestedState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        if(message.getCommand() == Command.CANCEL) {
            context.sendOperationCancelledMessage();
            return new DefaultState();
        }

        context.sendCronScheduleRequestedMessage();

        return new CronScheduleRequestedState(message.text);
    }
}

package bot.states;

import bot.command.Command;
import bot.models.ReceivedMessage;

class TaskNameRequestedState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        if (message.getCommand() == Command.CANCEL) {
            context.sendOperationCancelledMessage();
            return new DefaultState();
        }

        context.sendCronScheduleRequestedMessage();

        return new TaskScheduleRequestedState(message.text);
    }
}

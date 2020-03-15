package bot.states;

import bot.models.ReceivedMessage;
import bot.models.TaskListing;

class DefaultState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        switch (message.getCommand()) {
            case START:
                context.sendStartMessage();

                return this;
            case TASK:
                context.sendTaskNameRequestMessage();

                return new TaskNameRequestedState();
            case HELP:
                context.sendHelpMessage();

                return this;
            case TIMEZONE:
                context.sendTimezoneOffsetRequestedMessage(message.time);

                return new TimezoneOffsetRequestedState();
            case TASKS:
                TaskListing listing = new TaskListing(context.getTasks(), message.time);
                if (!listing.empty()) {
                    context.sendListOfTasksMessage(listing);
                    return new TasksListedState(listing);
                } else {
                    context.sendNoTasksMessage();
                    return this;
                }
            case CANCEL:
                context.sendNoOngoingOperationMessage();

                return this;
            case NOT_A_COMMAND:
                context.sendDefaultMessage();

                return this;
            case UNKNOWN:
                context.sendUnknownCommandMessage();

                return this;
            default:
                context.sendInvalidCommand();

                return this;
        }
    }
}
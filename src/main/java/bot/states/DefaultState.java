package bot.states;

import bot.entities.ReceivedMessage;

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
                return new TasksListedState(context);
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
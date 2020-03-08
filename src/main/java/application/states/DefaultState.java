package application.states;

import application.entities.ReceivedMessage;

class DefaultState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        switch (message.getCommand()) {
            case START:
                context.sendStartMessage();

                return this;
            case NEW_TASK:
                context.sendTaskNameRequestMessage();

                return new TaskNameRequestedState();
            case NEW_REMINDER:
                context.sendReminderNameRequestedMessage();

                return new ReminderNamedRequestedState();
            case HELP:
                context.sendHelpMessage();

                return this;
            case CANCEL:
                context.sendNoOngoingOperationMessage();

                return this;
            case COMMANDS:
                context.sendListOfCommandsMessage();

                return this;
            case UNKNOWN:
                context.sendUnknownCommandMessage();

                return this;
            default:
                context.sendDefaultMessage();

                return this;
        }
    }
}
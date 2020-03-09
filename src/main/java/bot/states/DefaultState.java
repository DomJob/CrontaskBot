package bot.states;

import bot.entities.ReceivedMessage;

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
            case SETTINGS:
                context.sendSettingsMenuMessage();

                return this;
            case TIMEZONE:
                context.sendTimezoneOffsetRequestedMessage();

                return new TimezoneOffsetRequestedState();
            default:
                context.sendDefaultMessage();

                return this;
        }
    }
}
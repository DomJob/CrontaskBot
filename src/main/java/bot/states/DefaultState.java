package bot.states;

import bot.command.Command;
import bot.models.ReceivedMessage;
import bot.models.TaskListing;
import domain.user.Language;

class DefaultState implements BotState {
    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        Command command = message.getCommand();
        switch (command) {
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
            case LANGUAGE:
                handleLanguageCommand(context, command);

                return this;
            case UNKNOWN:
                context.sendUnknownCommandMessage();

                return this;
            case NOT_A_MESSAGE:
                return this;
            default:
                context.sendInvalidCommand();

                return this;
        }
    }

    private void handleLanguageCommand(BotContext context, Command command) {
        if (command.getNbParameters() == 1) {
            context.sendLanguageInformationMessage();
        } else {
            String code = command.getParameter(1);

            try {
                Language language = Language.lookup(code);

                context.setLanguage(language);
                context.sendLanguageSetMessage();

            } catch (IllegalArgumentException e) {
                context.sendInvalidLanguageMessage();
            }
        }
    }
}
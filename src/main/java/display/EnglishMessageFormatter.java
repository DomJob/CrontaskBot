package display;

import bot.message.MessageFormatter;
import domain.task.Task;

public class EnglishMessageFormatter implements MessageFormatter {
    // Todo - Make these messages more pretty

    @Override
    public String formatTaskTriggeredMessage(Task task) {
        return "undefined message"; // TODO
    }

    @Override
    public String formatDefaultMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatUnknownCommandMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatNoOngoingOperationMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatHelpMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatTaskNameRequestMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatStartMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatOperationCancelledMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatInvalidScheduleFormat() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatTaskCreatedMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatScheduleRequestedMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatListOfCommandsMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatSettingsMenuMessage() {
        return "undefined message"; // TODO
    }

    @Override
    public String formatTimezoneOffsetRequestedMessage() {
        return "undefined message"; // TODO
    }
}

package display;

import bot.message.MessageFormatter;
import domain.task.Task;

public class FakeMessageFormatter implements MessageFormatter {
    @Override
    public String formatTaskTriggeredMessage(Task task) {
        return "message";
    }

    @Override
    public String formatDefaultMessage() {
        return "message";
    }

    @Override
    public String formatUnknownCommandMessage() {
        return "message";
    }

    @Override
    public String formatNoOngoingOperationMessage() {
        return "message";
    }

    @Override
    public String formatHelpMessage() {
        return "message";
    }

    @Override
    public String formatTaskNameRequestMessage() {
        return "message";
    }

    @Override
    public String formatStartMessage() {
        return "message";
    }

    @Override
    public String formatOperationCancelledMessage() {
        return "message";
    }

    @Override
    public String formatInvalidScheduleFormat() {
        return "message";
    }

    @Override
    public String formatTaskCreatedMessage() {
        return "message";
    }

    @Override
    public String formatScheduleRequestedMessage() {
        return "message";
    }

    @Override
    public String formatListOfCommandsMessage() {
        return "message";
    }

    @Override
    public String formatSettingsMenuMessage() {
        return "message";
    }

    @Override
    public String formatTimezoneOffsetRequestedMessage() {
        return "message";
    }
}

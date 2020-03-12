package bot.message;

import domain.task.Task;

public interface MessageFormatter {
    String formatTaskTriggeredMessage(Task task);

    String formatDefaultMessage();

    String formatUnknownCommandMessage();

    String formatNoOngoingOperationMessage();

    String formatHelpMessage();

    String formatTaskNameRequestMessage();

    String formatStartMessage();

    String formatOperationCancelledMessage();

    String formatInvalidScheduleFormat();

    String formatTaskCreatedMessage();

    String formatScheduleRequestedMessage();

    String formatListOfCommandsMessage();

    String formatSettingsMenuMessage();

    String formatTimezoneOffsetRequestedMessage();
}

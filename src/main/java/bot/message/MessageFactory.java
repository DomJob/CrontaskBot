package bot.message;

import domain.task.Task;

public interface MessageFactory {
    Message createTaskTriggeredMessage(Task task);

    Message createDefaultMessage();

    Message createUnknownCommandMessage();

    Message createNoOngoingOperationMessage();

    Message createHelpMessage();

    Message createTaskNameRequestMessage();

    Message createStartMessage();

    Message createOperationCancelledMessage();

    Message createInvalidScheduleFormat();

    Message createTaskCreatedMessage();

    Message createScheduleRequestedMessage();

    Message createListOfCommandsMessage();

    Message createSettingsMenuMessage();

    Message createTimezoneOffsetRequestedMessage();
}

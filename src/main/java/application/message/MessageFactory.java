package application.message;

import domain.Task;

public interface MessageFactory {
    Message createTaskTriggeredMessage(Task task);
    Message createDefaultMessage();
    Message createUnknownCommandMessage();
    Message createNoOngoingOperationMessage();
    Message createHelpMessage();
    Message createReminderNameRequestedMessage();
    Message createTaskNameRequestMessage();
    Message createStartMessage();
    Message createOperationCancelledMessage();
    Message createInvalidCronFormatMessage();
    Message createTaskCreatedMessage();
    Message createReminderTimeRequestedMessage();
    Message createInvalidReminderScheduleMessage();
    Message createReminderCreatedMessage();
    Message createCronScheduleRequestedMessage();
    Message createListOfCommandsMessage();
}

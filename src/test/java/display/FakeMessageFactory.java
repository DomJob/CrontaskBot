package display;

import bot.message.Message;
import bot.message.MessageFactory;
import domain.task.Task;

public class FakeMessageFactory implements MessageFactory {
    private Message defaultMessage = new Message("Fake");

    @Override
    public Message createTaskTriggeredMessage(Task task) {
        return defaultMessage;
    }

    @Override
    public Message createDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public Message createUnknownCommandMessage() {
        return defaultMessage;
    }

    @Override
    public Message createNoOngoingOperationMessage() {
        return defaultMessage;
    }

    @Override
    public Message createHelpMessage() {
        return defaultMessage;
    }

    @Override
    public Message createTaskNameRequestMessage() {
        return defaultMessage;
    }

    @Override
    public Message createStartMessage() {
        return defaultMessage;
    }

    @Override
    public Message createOperationCancelledMessage() {
        return defaultMessage;
    }

    @Override
    public Message createInvalidScheduleFormat() {
        return defaultMessage;
    }

    @Override
    public Message createTaskCreatedMessage() {
        return defaultMessage;
    }

    @Override
    public Message createScheduleRequestedMessage() {
        return defaultMessage;
    }

    @Override
    public Message createListOfCommandsMessage() {
        return defaultMessage;
    }

    @Override
    public Message createSettingsMenuMessage() {
        return defaultMessage;
    }

    @Override
    public Message createTimezoneOffsetRequestedMessage() {
        return defaultMessage;
    }
}

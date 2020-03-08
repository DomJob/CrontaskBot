package ui;

import application.Message;
import application.MessageFactory;
import domain.Task;

public class EnglishMessageFactory implements MessageFactory {
    // Todo - Make these messages more pretty

    private static Message message(String text) {
        return new Message(text);
    }

    @Override
    public Message createTaskTriggeredMessage(Task task) {
        return message(String.format("<b>Task:</b>\n\n%s", task.getName()));
    }

    @Override
    public Message createDefaultMessage() {
        return message("Use /commands for a list of commands or /help for more information.");
    }

    @Override
    public Message createUnknownCommandMessage() {
        return message("Unknown commands. Use /commands");
    }

    @Override
    public Message createNoOngoingOperationMessage() {
        return message("There is no operation to cancel!");
    }

    @Override
    public Message createHelpMessage() {
        return message("This is the help message");
    }

    @Override
    public Message createReminderNameRequestedMessage() {
        return message("Choose a name for the new reminder");
    }

    @Override
    public Message createTaskNameRequestMessage() {
        return message("Choose a name for the new task.");
    }

    @Override
    public Message createStartMessage() {
        return message("Welcome! Use /commands for a list of commands or /help to read more about this bot.");
    }

    @Override
    public Message createOperationCancelledMessage() {
        return message("Operation cancelled.");
    }

    @Override
    public Message createInvalidCronFormatMessage() {
        return message("Invalid cron format. Please try again.");
    }

    @Override
    public Message createTaskCreatedMessage() {
        return message("Task successfully created!");
    }

    @Override
    public Message createReminderTimeRequestedMessage() {
        return message("Choose a time for the new reminder.");
    }

    @Override
    public Message createInvalidReminderScheduleMessage() {
        return message("Invalid time. Please try again.");
    }

    @Override
    public Message createReminderCreatedMessage() {
        return message("Reminder created!");
    }

    @Override
    public Message createCronScheduleRequestedMessage() {
        return message("Choose a schedule for the new task.");
    }

    @Override
    public Message createListOfCommandsMessage() {
        return message("list of commands goes here");
    }
}

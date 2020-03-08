package application.states;

import application.BotState;
import application.CrontaskBot;
import application.Message;
import application.MessageFactory;
import application.entities.ReceivedMessage;
import domain.Schedule;

public class BotContext {
    private CrontaskBot bot;
    private MessageFactory messageFactory;
    private long userId;

    private BotState state = new DefaultState();

    public BotContext(CrontaskBot bot, long userId, MessageFactory messageFactory) {
        this.bot = bot;
        this.userId = userId;
        this.messageFactory = messageFactory;
    }

    public void handleMessage(ReceivedMessage message) {
        state = state.handleMessage(message, this);
    }

    protected void send(Message message) {
        message.setReceiver(userId);
        bot.sendMessage(message);
    }

    protected void createTask(String taskName, Schedule schedule) {
        bot.createTask(taskName, userId, schedule);
    }

    protected void createReminder(String reminderName, Schedule schedule) {
        bot.createReminder(reminderName, userId, schedule);
    }

    protected void sendDefaultMessage() {
        send(messageFactory.createDefaultMessage());
    }

    protected void sendUnknownCommandMessage() {
        send(messageFactory.createUnknownCommandMessage());
    }

    protected void sendNoOngoingOperationMessage() {
        send(messageFactory.createNoOngoingOperationMessage());
    }

    protected void sendHelpMessage() {
        send(messageFactory.createHelpMessage());
    }

    protected void sendReminderNameRequestedMessage() {
        send(messageFactory.createReminderNameRequestedMessage());
    }

    protected void sendTaskNameRequestMessage() {
        send(messageFactory.createTaskNameRequestMessage());
    }

    protected void sendStartMessage() {
        send(messageFactory.createStartMessage());
    }

    protected void sendOperationCancelledMessage() {
        send(messageFactory.createOperationCancelledMessage());
    }

    protected void sendInvalidCronFormatMessage() {
        send(messageFactory.createInvalidCronFormatMessage());
    }

    protected void sendTaskCreatedMessage() {
        send(messageFactory.createTaskCreatedMessage());
    }

    protected void sendReminderTimeRequestedMessage() {
        send(messageFactory.createReminderTimeRequestedMessage());
    }

    protected void sendInvalidReminderScheduleMessage() {
        send(messageFactory.createInvalidReminderScheduleMessage());
    }

    protected void sendReminderCreatedMessage() {
        send(messageFactory.createReminderCreatedMessage());
    }

    protected void sendCronScheduleRequestedMessage() {
        send(messageFactory.createCronScheduleRequestedMessage());
    }

    protected void sendListOfCommandsMessage() {
        send(messageFactory.createListOfCommandsMessage());
    }
}

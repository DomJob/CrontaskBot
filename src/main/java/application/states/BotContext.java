package application.states;

import application.CrontaskBot;
import application.entities.ReceivedMessage;
import application.message.Message;
import application.message.MessageFactory;
import domain.Schedule;
import domain.User;
import domain.time.Timezone;

public class BotContext {
    private CrontaskBot bot;
    private MessageFactory messageFactory;
    private User user;

    private BotState state = new DefaultState();

    public BotContext(CrontaskBot bot, User user, MessageFactory messageFactory) {
        this.bot = bot;
        this.user = user;
        this.messageFactory = messageFactory;
    }

    public void handleMessage(ReceivedMessage message) {
        state = state.handleMessage(message, this);
    }

    protected void send(Message message) {
        message.setReceiver(user);
        bot.sendMessage(message);
    }

    protected void createTask(String taskName, Schedule schedule) {
        bot.createTask(taskName, user, schedule);
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

    public void sendSettingsMenuMessage() {
        send(messageFactory.createSettingsMenuMessage());
    }

    public void sendTimezoneOffsetRequestedMessage() {
        send(messageFactory.createTimezoneOffsetRequestedMessage());
    }

    public void sendInvalidOperationMessage() {
        send(messageFactory.createInvalidOperationMessage());
    }

    public void setTimezoneOffset(Timezone timezone) {
        user.setTimezone(timezone);
    }
}

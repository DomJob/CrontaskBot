package application.states;

import application.CrontaskBot;
import application.entities.ReceivedMessage;
import application.message.Message;
import application.message.MessageFactory;
import domain.schedule.Schedule;
import domain.time.Timezone;
import domain.user.User;

public class BotContext {
    private CrontaskBot bot;
    private MessageFactory messageFactory;
    private User user;

    private BotState state = new DefaultState();

    public BotContext(CrontaskBot bot, User user) {
        this.bot = bot;
        this.user = user;
        this.messageFactory = bot.getMessageFactoryForUser(user);
    }

    public void handleMessage(ReceivedMessage message) {
        state = state.handleMessage(message, this);
    }

    public void setTimezone(Timezone timezone) {
        bot.setTimezoneForUser(user, timezone);
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

    protected void sendTaskNameRequestMessage() {
        send(messageFactory.createTaskNameRequestMessage());
    }

    protected void sendStartMessage() {
        send(messageFactory.createStartMessage());
    }

    protected void sendOperationCancelledMessage() {
        send(messageFactory.createOperationCancelledMessage());
    }

    protected void sendInvalidScheduleFormat() {
        send(messageFactory.createInvalidScheduleFormat());
    }

    protected void sendTaskCreatedMessage() {
        send(messageFactory.createTaskCreatedMessage());
    }

    protected void sendCronScheduleRequestedMessage() {
        send(messageFactory.createScheduleRequestedMessage());
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
}

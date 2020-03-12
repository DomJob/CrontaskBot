package bot.states;

import bot.CrontaskBot;
import bot.entities.ReceivedMessage;
import bot.message.Message;
import bot.message.MessageFactory;
import bot.message.MessageFactoryProvider;
import domain.schedule.Schedule;
import domain.time.Timezone;
import domain.user.User;
import service.TaskService;
import service.UserService;

public class BotContext {
    private CrontaskBot bot;
    private User user;
    private TaskService taskService;
    private UserService userService;
    private MessageFactory messageFactory;

    private BotState state = new DefaultState();

    public BotContext(CrontaskBot bot, User user, TaskService taskService, UserService userService, MessageFactoryProvider messageFactoryProvider) {
        this.bot = bot;
        this.taskService = taskService;
        this.userService = userService;
        this.user = user;
        this.messageFactory = messageFactoryProvider.provide(user.getLanguage());
    }

    public void handleMessage(ReceivedMessage message) {
        state = state.handleMessage(message, this);
    }

    public Timezone getTimezone() {
        return user.getTimezone();
    }

    public void setTimezone(Timezone timezone) {
        userService.changeTimezone(user, timezone);
    }

    protected void createTask(String name, Schedule schedule) {
        taskService.createTask(name, user, schedule);
    }

    protected void send(Message message) {
        message.setReceiver(user);
        bot.sendMessage(message);
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

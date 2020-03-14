package bot.states;

import bot.CrontaskBot;
import bot.entities.ReceivedMessage;
import bot.entities.TaskListing;
import bot.message.Message;
import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskId;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import java.util.List;
import service.TaskService;
import service.UserService;

public class BotContext {
    private CrontaskBot bot;
    private User user;
    private TaskService taskService;
    private UserService userService;
    private MessageFormatter messageFormatter;

    private BotState state = new DefaultState();

    public BotContext(CrontaskBot bot, User user, TaskService taskService, UserService userService, MessageFormatterProvider messageFormatterProvider) {
        this.bot = bot;
        this.taskService = taskService;
        this.userService = userService;
        this.user = user;
        this.messageFormatter = messageFormatterProvider.provide(user.getLanguage());
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
        bot.createTask(name, user, schedule);
    }

    protected void send(String text) {
        Message message = new Message(text, user);
        bot.sendMessage(message);
    }

    protected void sendDefaultMessage() {
        send(messageFormatter.formatDefaultMessage());
    }

    protected void sendUnknownCommandMessage() {
        send(messageFormatter.formatUnknownCommandMessage());
    }

    protected void sendNoOngoingOperationMessage() {
        send(messageFormatter.formatNoOngoingOperationMessage());
    }

    protected void sendHelpMessage() {
        send(messageFormatter.formatHelpMessage());
    }

    protected void sendTaskNameRequestMessage() {
        send(messageFormatter.formatTaskNameRequestMessage());
    }

    protected void sendStartMessage() {
        send(messageFormatter.formatStartMessage());
    }

    protected void sendOperationCancelledMessage() {
        send(messageFormatter.formatOperationCancelledMessage());
    }

    protected void sendInvalidScheduleFormat() {
        send(messageFormatter.formatInvalidScheduleFormat());
    }

    protected void sendTaskCreatedMessage() {
        send(messageFormatter.formatTaskCreatedMessage());
    }

    protected void sendCronScheduleRequestedMessage() {
        send(messageFormatter.formatScheduleRequestedMessage());
    }

    public void sendTimezoneOffsetRequestedMessage(Time now) {
        send(messageFormatter.formatTimezoneOffsetRequestedMessage(user.getTimezone(), now));
    }

    public void sendTimezoneSetMessage() {
        send(messageFormatter.formatTimezoneSetMessage());
    }

    public void sendInvalidTimezoneMessage() {
        send(messageFormatter.formatInvalidTimezoneMessage());
    }

    public void sendInvalidCommand() {
        send(messageFormatter.formatInvalidCommand());
    }

    public void sendListOfTasksMessage(TaskListing listing) {
        send(messageFormatter.formatTaskListingMessage(listing));
    }

    public List<Task> getTasks() {
        return taskService.getTasksForUser(user);
    }

    public void sendNoTasksMessage() {
        send(messageFormatter.formatNoTasksMessage());
    }

    public void deleteTask(Task task) {
        taskService.deleteTask(task);
    }
}

package bot.states;

import bot.CrontaskBot;
import bot.message.Message;
import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import bot.models.ReceivedMessage;
import bot.models.TaskListing;
import domain.schedule.Schedule;
import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.Language;
import domain.user.User;
import service.TaskService;
import service.UserService;

import java.util.List;

public class BotContext {
    private final CrontaskBot bot;
    private final User user;
    private final TaskService taskService;
    private final UserService userService;
    private final MessageFormatterProvider messageFormatterProvider;
    private MessageFormatter messageFormatter;

    private BotState state = new DefaultState();

    public BotContext(CrontaskBot bot, User user, TaskService taskService, UserService userService, MessageFormatterProvider messageFormatterProvider) {
        this.bot = bot;
        this.taskService = taskService;
        this.userService = userService;
        this.user = user;
        this.messageFormatterProvider = messageFormatterProvider;
        this.messageFormatter = messageFormatterProvider.provide(user.getLanguage());
    }

    public void handleMessage(ReceivedMessage message) {
        state = state.handleMessage(message, this);
    }

    protected Timezone getTimezone() {
        return user.getTimezone();
    }

    protected void setTimezone(Timezone timezone) {
        userService.changeTimezone(user, timezone);
    }

    protected void setLanguage(Language language) {
        userService.changeLanguage(user, language);
        this.messageFormatter = messageFormatterProvider.provide(language);
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

    protected void sendTimezoneOffsetRequestedMessage(Time now) {
        send(messageFormatter.formatTimezoneOffsetRequestedMessage(user.getTimezone(), now));
    }

    protected void sendTimezoneSetMessage() {
        send(messageFormatter.formatTimezoneSetMessage());
    }

    protected void sendInvalidTimezoneMessage() {
        send(messageFormatter.formatInvalidTimezoneMessage());
    }

    protected void sendInvalidCommand() {
        send(messageFormatter.formatInvalidCommand());
    }

    protected void sendInvalidCommandDuringListing() {
        send(messageFormatter.formatInvalidCommandDuringListing());
    }

    protected void sendListOfTasksMessage(TaskListing listing) {
        send(messageFormatter.formatTaskListingMessage(listing));
    }

    protected List<Task> getTasks() {
        return taskService.getTasksForUser(user);
    }

    protected void sendNoTasksMessage() {
        send(messageFormatter.formatNoTasksMessage());
    }

    protected void deleteTask(Task task) {
        taskService.deleteTask(task);
    }

    protected void sendDeletedTaskMessage() {
        send(messageFormatter.formatTaskDeletedMessage());
    }

    protected void sendInvalidDeleteCommand() {
        send(messageFormatter.formatInvalidDeleteCommand());
    }

    protected void sendLanguageInformationMessage() {
        send(messageFormatter.formatLanguageInformationMessage());
    }

    protected void sendInvalidLanguageMessage() {
        send(messageFormatter.formatInvalidLangageMessage());
    }

    protected void sendLanguageSetMessage() {
        send(messageFormatter.formatLanguageSetMessage());
    }
}

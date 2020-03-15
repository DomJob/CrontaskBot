package bot;

import bot.command.CallbackCommand;
import bot.message.Message;
import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import bot.models.CallbackQuery;
import bot.models.ReceivedMessage;
import bot.states.BotContext;
import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskId;
import domain.time.Time;
import domain.user.User;
import domain.user.UserId;
import java.util.HashMap;
import java.util.Map;
import service.TaskService;
import service.UserService;

public class CrontaskBot {
    private TelegramApi api;
    private TaskService taskService;
    private UserService userService;
    private MessageFormatterProvider messageFormatterProvider;

    private Map<UserId, BotContext> contexts = new HashMap<>();

    public CrontaskBot(TelegramApi api, TaskService taskService, UserService userService, MessageFormatterProvider messageFormatterProvider) {
        this.api = api;
        this.taskService = taskService;
        this.userService = userService;
        this.messageFormatterProvider = messageFormatterProvider;
    }

    public void handleMessage(ReceivedMessage message) {
        BotContext context = getContextForUser(message.userId);
        context.handleMessage(message);
    }

    public void handleCallbackQuery(CallbackQuery query) {
        CallbackCommand command = CallbackCommand.parse(query.data);

        TaskId id = TaskId.fromString(command.getParameters().get(1));

        Time now = query.time;

        switch (command) {
            case SNOOZE:
                Task task = taskService.getTask(id);

                if (!task.getOwner().getId().equals(query.userId)) {
                    api.answerCallbackQuery(query.id, "How did you even do this?");
                    return;
                }

                taskService.snoozeTask(task, now);
                api.answerCallbackQuery(query.id, "Snoozed for 15 minutes");
                api.deleteMessage(query.userId, query.messageId);
                break;
            case DISMISS:
                taskService.dismissTask(id, now);
                api.answerCallbackQuery(query.id, "Task dismissed");
                api.deleteMessage(query.userId, query.messageId);
        }
    }

    public void sendMessage(Message message) {
        api.sendMessage(message);
    }

    public void notifyTaskTriggered(Task task) {
        User user = task.getOwner();

        MessageFormatter messageFormatter = messageFormatterProvider.provide(user.getLanguage());

        String text = messageFormatter.formatTaskTriggeredMessage(task);

        Message message = new Message(text, user);
        message.addButton("Dismiss", "dismiss " + task.getId());
        message.addButton("Snooze", "snooze " + task.getId());

        sendMessage(message);
    }

    public void createTask(String name, User owner, Schedule schedule) {
        taskService.createTask(name, owner, schedule);
    }

    private BotContext getContextForUser(UserId id) {
        if (!contexts.containsKey(id)) {
            User user = userService.getOrCreateUser(id);
            BotContext context = new BotContext(this, user, taskService, userService, messageFormatterProvider);
            contexts.put(id, context);
        }

        return contexts.get(id);
    }
}
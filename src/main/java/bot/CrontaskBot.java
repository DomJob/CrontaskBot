package bot;

import bot.command.CallbackCommand;
import bot.entities.CallbackQuery;
import bot.entities.ReceivedMessage;
import bot.message.Message;
import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import bot.states.BotContext;
import domain.task.Task;
import domain.user.User;
import java.util.HashMap;
import java.util.Map;
import service.TaskService;
import service.UserService;

public class CrontaskBot {
    private TelegramApi api;
    private TaskService taskService;
    private UserService userService;
    private MessageFormatterProvider messageFormatterProvider;

    private Map<Long, BotContext> contexts = new HashMap<>();

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

        long id = Long.parseLong(command.getParameters().get(1));

        switch (command) {
            case SNOOZE:
                taskService.snoozeTask(id, query.time);
                api.answerCallbackQuery(query.id, "Snoozed for 15 minutes");
                api.deleteMessage(query.userId, query.messageId);
                break;
            case DISMISS:
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

    private BotContext getContextForUser(long userId) {
        if (!contexts.containsKey(userId)) {
            User user = userService.getOrCreateUser(userId);
            BotContext context = new BotContext(this, user, taskService, userService, messageFormatterProvider);
            contexts.put(userId, context);
        }

        return contexts.get(userId);
    }
}
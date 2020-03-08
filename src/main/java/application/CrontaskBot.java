package application;

import application.command.CallbackCommand;
import application.entities.CallbackQuery;
import application.entities.ReceivedMessage;
import application.message.Message;
import application.message.MessageFactory;
import application.message.MessageFactoryProvider;
import application.states.BotContext;
import domain.Schedule;
import domain.Task.Task;
import domain.Task.TaskFactory;
import domain.Task.TaskRepository;
import domain.reminderschedule.ReminderSchedule;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;

public class CrontaskBot {
    private TelegramApi api;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskFactory taskFactory;
    private MessageFactoryProvider messageFactoryProvider;

    private Map<Long, BotContext> contexts = new HashMap<>();

    public CrontaskBot(TelegramApi api, TaskRepository taskRepository, UserRepository userRepository, TaskFactory taskFactory, MessageFactoryProvider messageFactoryProvider) {
        this.api = api;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskFactory = taskFactory;
        this.messageFactoryProvider = messageFactoryProvider;
    }

    public void handleMessage(ReceivedMessage message) {
        long sender = message.userId;

        BotContext context = getOrCreateContextForUser(sender);

        context.handleMessage(message);
    }

    public void handleCallbackQuery(CallbackQuery query) {
        CallbackCommand command = CallbackCommand.parse(query.data);

        Task task = taskRepository.findById(Long.parseLong(command.getParameters().get(1)));

        switch (command) {
            case SNOOZE:
                api.answerCallbackQuery(query.id, "Snoozed for 15 minutes");
                createTask(task.getName(), task.getOwner(), ReminderSchedule.minutesFromNow(15));
            case DISMISS:
                api.deleteMessage(query.userId, query.messageId);
                break;
        }
    }

    public void sendMessage(Message message) {
        api.sendMessage(message);
    }

    public void createTask(String name, User owner, Schedule schedule) {
        Task task = taskFactory.create(name, owner, schedule);

        taskRepository.save(task);
    }

    public void setTimezoneForUser(User user, Timezone timezone) {
        user.setTimezone(timezone);

        userRepository.save(user);
    }

    public void checkTasks(Time time) {
        for (Task task : taskRepository.findAll()) {
            if (task.isTriggered(time)) {
                User user = task.getOwner();

                Message message = getMessageFactoryForUser(user).createTaskTriggeredMessage(task);
                message.setReceiver(user);

                message.addButton("Dismiss", "dismiss " + task.getId());
                message.addButton("Snooze", "snooze " + task.getId());

                api.sendMessage(message);
            }
        }
    }

    public MessageFactory getMessageFactoryForUser(User user) {
        return messageFactoryProvider.provide(user.getLanguage());
    }

    private BotContext getOrCreateContextForUser(long userId) {
        if (!contexts.containsKey(userId)) {
            User user = userRepository.findById(userId);

            BotContext context = new BotContext(this, user);

            contexts.put(userId, context);
        }

        return contexts.get(userId);
    }
}
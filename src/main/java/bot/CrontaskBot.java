package bot;

import bot.command.CallbackCommand;
import bot.entities.CallbackQuery;
import bot.entities.ReceivedMessage;
import bot.message.Message;
import bot.message.MessageFactory;
import bot.message.MessageFactoryProvider;
import bot.states.BotContext;
import domain.schedule.Schedule;
import domain.schedule.TimeSchedule;
import domain.task.Task;
import domain.task.TaskFactory;
import domain.task.TaskRepository;
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

                Time snoozeUntil = query.time.plusMinutes(15);

                createTask(task.getName(), task.getOwner(), new TimeSchedule(snoozeUntil));
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

    public MessageFactory getMessageFactoryForUser(User user) {
        return messageFactoryProvider.provide(user.getLanguage());
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

    private BotContext getOrCreateContextForUser(long userId) {
        if (!contexts.containsKey(userId)) {
            User user = userRepository.findById(userId);
            if(user == null) {
                user = new User(userId);
                userRepository.save(user);
            }

            BotContext context = new BotContext(this, user);

            contexts.put(userId, context);
        }

        return contexts.get(userId);
    }
}
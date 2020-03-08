package application;

import application.entities.CallbackQuery;
import application.entities.ReceivedMessage;
import application.entities.Update;
import application.states.BotContext;
import domain.Schedule;
import domain.Task;
import domain.TaskFactory;
import domain.TaskRepository;
import domain.Time;
import java.util.HashMap;
import java.util.Map;

public class CrontaskBot {
    private TelegramApi api;
    private TaskRepository taskRepository;
    private TaskFactory taskFactory;
    private MessageFactory messageFactory;

    private Map<Long, BotContext> contexts = new HashMap<>();

    private long lastUpdate;

    public CrontaskBot(TelegramApi api, TaskRepository taskRepository, TaskFactory taskFactory, MessageFactory messageFactory) {
        this.api = api;
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
        this.messageFactory = messageFactory;
    }

    public void handleMessage(ReceivedMessage message) {
        long sender = message.user;

        if(!contexts.containsKey(sender)) {
            BotContext context = new BotContext(this, sender, messageFactory);
            contexts.put(sender, context);
        }

        contexts.get(sender).handleMessage(message);
    }

    public void handleCallbackQuery(CallbackQuery query) {
        // Todo
        System.out.println("I am handling a call back query: " + query.data);
    }

    public void sendMessage(Message message) {
        api.sendMessage(message);
    }

    public void createTask(String name, long ownerId, Schedule schedule) {
        Task task = taskFactory.create(name, ownerId, schedule, false);

        taskRepository.save(task);
    }

    public void createReminder(String name, long ownerId, Schedule schedule) {
        Task task = taskFactory.create(name, ownerId, schedule, true);

        taskRepository.save(task);
    }

    public void checkTasks() {
        checkTasksAtTime(Time.now());
    }

    public void checkTasksAtTime(Time time) {
        for(Task task : taskRepository.findAll()) {
            if(task.isTriggered(time)) {
                Message message = messageFactory.createTaskTriggeredMessage(task);
                message.setReceiver(task.getOwnerId());

                message.addButton("Dismiss", "dismiss" + task.getId());
                message.addButton("Snooze", "snooze" + task.getId());

                api.sendMessage(message);

                if(task.isReminder()) {
                    taskRepository.delete(task);
                }
            }
        }
    }

    public void handleEvents() {
        for(Update update : api.getUpdates(lastUpdate)) {
            switch(update.type) {
                case MESSAGE:
                    handleMessage(update.message);
                    break;
                case CALLBACK:
                    handleCallbackQuery(update.callbackQuery);
                    break;
            }
            lastUpdate = update.id + 1;
        }
    }
}
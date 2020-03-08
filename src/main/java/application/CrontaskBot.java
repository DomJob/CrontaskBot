package application;

import application.command.CallbackCommand;
import application.entities.CallbackQuery;
import application.entities.ReceivedMessage;
import application.message.Message;
import application.message.MessageFactory;
import application.states.BotContext;
import domain.Schedule;
import domain.Task;
import domain.TaskFactory;
import domain.TaskRepository;
import domain.User;
import domain.reminderschedule.ReminderSchedule;
import domain.time.Time;
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
        long sender = message.userId;

        if(!contexts.containsKey(sender)) {
            BotContext context = new BotContext(this, new User(sender), messageFactory);
            contexts.put(sender, context);
        }

        contexts.get(sender).handleMessage(message);
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

    public void checkTasks(Time time) {
        for(Task task : taskRepository.findAll()) {
            if(task.isTriggered(time)) {
                Message message = messageFactory.createTaskTriggeredMessage(task);
                message.setReceiver(task.getOwner());

                message.addButton("Dismiss", "dismiss " + task.getId());
                message.addButton("Snooze", "snooze " + task.getId());

                api.sendMessage(message);
            }
        }
    }
}
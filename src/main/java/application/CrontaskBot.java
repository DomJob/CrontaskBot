package application;

import application.entities.CallbackQuery;
import application.entities.Message;
import application.states.DefaultState;
import domain.Schedule;
import domain.Task;
import domain.TaskFactory;
import domain.TaskRepository;
import java.util.HashMap;
import java.util.Map;

public class CrontaskBot {
    private TelegramApi api;
    private TaskRepository taskRepository;
    private TaskFactory taskFactory;

    private Map<Long, BotState> states = new HashMap<>();

    public CrontaskBot(TelegramApi api, TaskRepository taskRepository, TaskFactory taskFactory) {
        this.api = api;
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
    }

    public void handleMessage(Message message) {
        BotState state = states.getOrDefault(message.sender, new DefaultState());

        states.put(message.sender, state.handleMessage(message, this));
    }

    public void handleCallbackQuery(CallbackQuery query) {

    }

    public void sendMessage(long user, String text) {
        api.sendMessage(user, text);
    }

    public void createTask(String name, long ownerId, Schedule schedule) {
        Task task = taskFactory.create(name, ownerId, schedule, false);

        taskRepository.save(task);
    }

    public void createReminder(String name, long ownerId, Schedule schedule) {
        Task task = taskFactory.create(name, ownerId, schedule, true);

        taskRepository.save(task);
    }
}
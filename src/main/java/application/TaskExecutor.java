package application;

import configuration.Messages;
import domain.Task;
import domain.TaskRepository;
import domain.Time;

public class TaskExecutor implements Runnable {
    private TelegramApi api;
    private TaskRepository repository;

    public TaskExecutor(TelegramApi api, TaskRepository repository) {
        this.api = api;
        this.repository = repository;
    }

    @Override
    public void run() {
        Time now = Time.now();

        for(Task task : repository.findAll()) {
            if(task.isTriggered(now)) {
                api.sendMessage(task.getOwnerId(), Messages.taskTriggeredMessage(task.getName()));

                if(task.isReminder()) {
                    repository.delete(task);
                }
            }
        }
    }
}

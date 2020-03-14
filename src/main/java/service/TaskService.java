package service;

import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskFactory;
import domain.task.TaskId;
import domain.task.TaskRepository;
import domain.time.Time;
import domain.user.User;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private TaskFactory taskFactory;
    private TaskRepository taskRepository;

    public TaskService(TaskFactory taskFactory, TaskRepository taskRepository) {
        this.taskFactory = taskFactory;
        this.taskRepository = taskRepository;
    }

    public void createTask(String name, User user, Schedule schedule) {
        Task task = taskFactory.create(name, user, schedule);
        taskRepository.save(task);
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.getTasksForUser(user.getId());
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task.getId());
    }

    public void snoozeTask(Task task, Time now) {
        task.snoozeUntil(now.plusMinutes(15));
        taskRepository.save(task);
    }

    public void dismissTask(TaskId id, Time now) {
        Task task = getTask(id);

        if (task.scheduledFor(now).equals(Time.NEVER)) {
            deleteTask(task);
        }
    }

    public void checkTasks(Time now, TaskNotifier taskNotifier) {
        for (Task task : taskRepository.findAll()) {
            if (task.isTriggered(now)) {
                taskNotifier.notifyTaskTriggered(task);
            }
        }
    }

    public Task getTask(TaskId id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            return task.get();
        }

        throw new TaskNotFoundException();
    }
}

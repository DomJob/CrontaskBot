package service;

import domain.schedule.Schedule;
import domain.schedule.TimeSchedule;
import domain.task.Task;
import domain.task.TaskFactory;
import domain.task.TaskId;
import domain.task.TaskRepository;
import domain.time.Time;
import domain.user.User;
import java.util.List;

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
        return null; // TODO
    }

    public void deleteTask(TaskId id) {
        // TODO
    }

    public void snoozeTask(TaskId id, Time now) {
        Task task = taskRepository.findById(id).get();

        Time snoozeUntil = now.plusMinutes(15);

        Task newTask = taskFactory.create(task.getName(), task.getOwner(), new TimeSchedule(snoozeUntil)); // TODO Yeah that's a problem right here
        taskRepository.save(newTask);
    }

    public void checkTasks(Time now, TaskNotifier taskNotifier) {
        for (Task task : taskRepository.findAll()) {
            if (task.isTriggered(now)) {
                taskNotifier.notifyTaskTriggered(task);
            }
        }
    }
}

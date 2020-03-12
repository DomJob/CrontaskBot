package service;

import domain.task.Task;

public interface TaskNotifier {
    void notifyTaskTriggered(Task task);
}

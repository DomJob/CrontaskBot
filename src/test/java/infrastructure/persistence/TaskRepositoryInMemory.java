package infrastructure.persistence;

import domain.task.Task;
import domain.task.TaskId;
import domain.task.TaskRepository;
import domain.user.UserId;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskRepositoryInMemory implements TaskRepository {
    private Map<TaskId, Task> tasks = new HashMap<>();

    @Override
    public Collection<Task> findAll() {
        return tasks.values();
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void delete(TaskId id) {
        tasks.remove(id);
    }

    @Override
    public List<Task> getTasksForUser(UserId id) {
        return tasks.values().stream().filter(t -> t.getOwner().getId().equals(id)).collect(Collectors.toList());
    }
}

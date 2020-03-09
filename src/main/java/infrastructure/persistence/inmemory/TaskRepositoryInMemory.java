package infrastructure.persistence.inmemory;

import domain.task.Task;
import domain.task.TaskRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskRepositoryInMemory implements TaskRepository {
    // TODO - Actual persistence and move InMemory for IT Test

    private Map<Long, Task> tasks = new HashMap<>();

    @Override
    public Collection<Task> findAll() {
        return tasks.values();
    }

    @Override
    public Task findById(long id) {
        return tasks.get(id);
    }

    @Override
    public void save(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void delete(Task task) {
        tasks.remove(task.getId());
    }
}

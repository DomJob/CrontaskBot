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

public class TaskRepositoryCache implements TaskRepository {
    TaskRepository repository;
    Map<TaskId, Task> cache = new HashMap<>();

    public TaskRepositoryCache(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Task> findAll() {
        if(cache.isEmpty()) {
            for(Task task : repository.findAll()) {
                cache.put(task.getId(), task);
            }
        }

        return cache.values();
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        Task found = cache.get(id);

        if(found == null) {
            Optional<Task> task = repository.findById(id);
            if(task.isPresent()) {
                found = task.get();
                cache.put(id, found);
            }
        }

        return Optional.ofNullable(found);
    }

    @Override
    public void save(Task task) {
        cache.putIfAbsent(task.getId(), task);
        repository.save(task);
    }

    @Override
    public void delete(TaskId id) {
        cache.remove(id);
        repository.delete(id);
    }

    @Override
    public List<Task> getTasksForUser(UserId id) {
        return findAll().stream().filter(t -> t.getOwner().getId().equals(id)).collect(Collectors.toList());
    }
}

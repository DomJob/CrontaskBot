package domain.task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Collection<Task> findAll();

    Optional<Task> findById(TaskId id);

    void save(Task task);
}

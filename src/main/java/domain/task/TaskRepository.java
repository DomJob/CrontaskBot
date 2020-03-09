package domain.task;

import java.util.Collection;

public interface TaskRepository {
    Collection<Task> findAll();

    Task findById(long id);

    void save(Task task);

    void delete(Task task);
}

package domain.task;

import domain.user.UserId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Collection<Task> findAll();

    Optional<Task> findById(TaskId id);

    void save(Task task);

    void delete(TaskId id);

    List<Task> getTasksForUser(UserId id);
}

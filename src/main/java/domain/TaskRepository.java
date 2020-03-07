package domain;

import java.util.List;

public interface TaskRepository {
    List<Task> findAll();

    Task findById(long id);

    void save(Task task);

    void delete(Task task);
}

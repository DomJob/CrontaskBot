package infrastructure.persistence;

import static infrastructure.persistence.Sqlite.getConnection;

import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskRepository;
import domain.time.Timezone;
import domain.user.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TaskRepositorySQL implements TaskRepository {
    private static final String FIND_TASK_BY_ID = "SELECT * FROM task WHERE id=?";
    private static final String FIND_USER_BY_ID = "SELECT tzOffset FROM user WHERE id = ?";
    private static final String FIND_ALL_TASKS = "SELECT * FROM task";
    private static final String INSERT_TASK = "INSERT INTO task VALUES(?, ?, ?, ?)";

    @Override
    public Collection<Task> findAll() {
        List<Task> tasks = new ArrayList<>();

        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(FIND_ALL_TASKS);

            if (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                long owner = rs.getLong("owner");
                String schedule = rs.getString("schedule");

                Task task = new Task(id, name, findUser(owner), Schedule.deserialize(schedule));

                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    @Override
    public Optional<Task> findById(long id) {
        Task task = null;

        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_TASK_BY_ID);

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                long owner = rs.getLong("owner");
                String schedule = rs.getString("schedule");

                task = new Task(id, name, findUser(owner), Schedule.deserialize(schedule));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(task);
    }

    @Override
    public void save(Task task) {
        Optional<Task> existingTask = findById(task.getId());
        if (!existingTask.isPresent()) {
            insertTask(task);
        }
    }

    private void insertTask(Task task) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_TASK);

            statement.setLong(1, task.getId());
            statement.setString(2, task.getName());
            statement.setLong(3, task.getOwner().getId());
            statement.setString(4, task.serializeSchedule());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private User findUser(long id) { // TODO Refactor query to use a fancy join and what not
        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_USER_BY_ID);

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int tzOffset = rs.getInt("tzOffset");

                return new User(id, Timezone.fromOffset(tzOffset));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

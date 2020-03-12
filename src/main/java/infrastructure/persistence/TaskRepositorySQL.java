package infrastructure.persistence;

import static infrastructure.persistence.Sqlite.getConnection;

import domain.task.Task;
import domain.task.TaskRepository;
import infrastructure.persistence.entities.TaskDao;
import infrastructure.persistence.entities.UserDao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskRepositorySQL implements TaskRepository {
    private static final String FIND_TASK_BY_ID = "SELECT * FROM task WHERE id=?";
    private static final String FIND_USER_BY_ID = "SELECT tzOffset FROM user WHERE id = ?";
    private static final String FIND_ALL_TASKS = "SELECT * FROM task";
    private static final String INSERT_TASK = "INSERT INTO task VALUES(?, ?, ?, ?)";

    @Override
    public Collection<Task> findAll() {
        List<TaskDao> daos = new ArrayList<>();

        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(FIND_ALL_TASKS);

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                long owner = rs.getLong("owner");
                String schedule = rs.getString("schedule");

                TaskDao dao = new TaskDao(id, name, findUser(owner), schedule);
                daos.add(dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daos.stream().map(TaskDao::toModel).collect(Collectors.toList());
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

                TaskDao dao = new TaskDao(id, name, findUser(owner), schedule);

                task = dao.toModel();
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

            TaskDao dao = TaskDao.fromModel(task);

            statement.setLong(1, dao.id);
            statement.setString(2, dao.name);
            statement.setLong(3, dao.owner.id);
            statement.setString(4, dao.schedule);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private UserDao findUser(long id) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_USER_BY_ID);

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int tzOffset = rs.getInt("tzOffset");
                return new UserDao(id, tzOffset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

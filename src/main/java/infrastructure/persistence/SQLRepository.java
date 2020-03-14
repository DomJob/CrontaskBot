package infrastructure.persistence;

import static infrastructure.persistence.Sqlite.getConnection;

import domain.task.Task;
import domain.task.TaskId;
import domain.task.TaskRepository;
import domain.user.User;
import domain.user.UserId;
import domain.user.UserRepository;
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

public class SQLRepository implements TaskRepository, UserRepository {
    private static final String FIND_TASK_BY_ID = "SELECT * FROM task WHERE id=?";
    private static final String FIND_TASK_BY_OWNER_ID = "SELECT * FROM task WHERE owner=?";
    private static final String FIND_USER_BY_ID = "SELECT tzOffset FROM user WHERE id = ?";
    private static final String FIND_ALL_TASKS = "SELECT * FROM task";
    private static final String INSERT_TASK = "INSERT INTO task VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_USER = "INSERT INTO user VALUES(?, ?)";
    private static final String UPDATE_USER = "UPDATE user SET tzOffset = ? WHERE id = ?";
    private static final String UPDATE_TASK = "UPDATE task SET snoozedUntil = ? WHERE id = ?";
    private static final String DELETE_TASK = "DELETE FROM tasks WHERE id = ?";

    @Override
    public Collection<Task> findAll() {
        List<TaskDao> daos = new ArrayList<>();

        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery(FIND_ALL_TASKS);

            while (rs.next()) {
                TaskDao dao = parseTaskResult(rs);
                daos.add(dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daos.stream().map(TaskDao::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        Task task = null;

        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_TASK_BY_ID);

            statement.setLong(1, id.toLong());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                TaskDao dao = parseTaskResult(rs);

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
        if (existingTask.isPresent()) {
            updateTask(task);
        } else {
            insertTask(task);
        }
    }

    @Override
    public void delete(TaskId id) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(DELETE_TASK);

            statement.setLong(1, id.toLong());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getTasksForUser(UserId id) {
        List<TaskDao> daos = new ArrayList<>();

        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_TASK_BY_OWNER_ID);
            statement.setLong(1, id.toLong());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                TaskDao dao = parseTaskResult(rs);
                daos.add(dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daos.stream().map(TaskDao::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UserId id) {
        User user = null;

        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_USER_BY_ID);

            statement.setLong(1, id.toLong());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int tzOffset = rs.getInt("tzOffset");

                UserDao dao = new UserDao(id.toLong(), tzOffset);

                user = dao.toModel();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        if (findById(user.getId()).isPresent()) {
            updateUser(user);
        } else {
            insertUser(user);
        }
    }

    private TaskDao parseTaskResult(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        long ownerId = rs.getLong("owner");
        String schedule = rs.getString("schedule");
        long snoozedUntil = rs.getLong("snoozedUntil");

        User owner = findById(new UserId(ownerId)).get();
        UserDao ownerDao = UserDao.fromModel(owner);

        return new TaskDao(id, name, ownerDao, schedule, snoozedUntil);
    }

    private void updateUser(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(UPDATE_USER);
            UserDao dao = UserDao.fromModel(user);

            statement.setInt(1, dao.tzOffset);
            statement.setLong(2, dao.id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUser(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_USER);
            UserDao dao = UserDao.fromModel(user);

            statement.setLong(1, dao.id);
            statement.setInt(2, dao.tzOffset);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateTask(Task task) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(UPDATE_TASK);
            TaskDao dao = TaskDao.fromModel(task);

            statement.setLong(1, dao.snoozedUntil);
            statement.setLong(2, dao.id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            statement.setLong(5, dao.snoozedUntil);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

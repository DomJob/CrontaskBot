package infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import domain.schedule.CronSchedule;
import domain.task.Task;
import domain.task.TaskRepository;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import domain.user.UserRepository;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SQLTest {
    private static final long ANY_EPOCH = 12345678;
    private static final Time ANY_TIME = new Time(ANY_EPOCH);

    private static final User USER = new User(98765, Timezone.fromOffset(4321));
    private static final Task TASK_1 = new Task(123, "task 1", USER, CronSchedule.parse("1 2 3 4 5"));
    private static final Task TASK_2 = new Task(456, "task 2", USER, CronSchedule.parse("5 4 3 2 1"));

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        Sqlite.setPath("crontaskbot_test.db");
        taskRepository = new TaskRepositorySQL();
        userRepository = new UserRepositorySQL();
    }

    @After
    public void tearDown() throws SQLException {
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM task");
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM user");
    }

    @Test
    public void saveAndRetrieveUser() {
        userRepository.save(USER);

        User retrieved = userRepository.findById(USER.getId()).get();

        assertSameUser(USER, retrieved);
    }

    @Test
    public void userNotFound_returnsNull() {
        Optional<User> retrieved = userRepository.findById(USER.getId());

        assertFalse(retrieved.isPresent());
    }

    @Test
    public void saveAndRetrieveTask() {
        userRepository.save(USER);

        taskRepository.save(TASK_1);

        Task retrieved = taskRepository.findById(TASK_1.getId()).get();

        assertSameTask(TASK_1, retrieved);
    }

    @Test
    public void saveAndRetrieveMany() {
        userRepository.save(USER);

        taskRepository.save(TASK_1);
        taskRepository.save(TASK_2);

        Collection<Task> retrieved = taskRepository.findAll();

        assertEquals(2, retrieved.size());
    }

    @Test
    public void taskNotFound_returnsNull() {
        Optional<Task> retrieved = taskRepository.findById(TASK_1.getId());

        assertFalse(retrieved.isPresent());
    }

    private void assertSameTask(Task expected, Task actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertSameUser(expected.getOwner(), actual.getOwner());
    }

    private void assertSameUser(User expected, User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTimezone().getOffset(), actual.getTimezone().getOffset());
    }
}
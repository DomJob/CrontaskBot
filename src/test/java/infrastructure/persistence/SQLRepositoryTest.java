package infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import domain.schedule.CronSchedule;
import domain.task.Task;
import domain.task.TaskId;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import domain.user.UserId;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class SQLRepositoryTest {
    public static final String TEST_DB_FILE_PATH = "crontaskbot_test.db";
    private static final long ANY_EPOCH = 12345678;
    private static final Time ANY_TIME = new Time(ANY_EPOCH);
    private static final User USER = new User(new UserId(98765), Timezone.fromOffset(4321));
    private static final Task TASK_1 = new Task(new TaskId(123), "task 1", USER, CronSchedule.parse("1 2 3 4 5"));
    private static final Task TASK_2 = new Task(new TaskId(456), "task 2", USER, CronSchedule.parse("5 4 3 2 1"));
    private SQLRepository repo = new SQLRepository();

    @AfterClass
    public static void cleanUpFile() {
        new File(TEST_DB_FILE_PATH).delete();
    }

    @Before
    public void setUp() {
        Sqlite.setPath(TEST_DB_FILE_PATH);
    }

    @After
    public void tearDown() throws SQLException {
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM task");
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM user");
    }

    @Test
    public void saveAndRetrieveUser() {
        repo.save(USER);

        User retrieved = repo.findById(USER.getId()).get();

        assertSameUser(USER, retrieved);
    }

    @Test
    public void userNotFound_returnsNull() {
        Optional<User> retrieved = repo.findById(USER.getId());

        assertFalse(retrieved.isPresent());
    }

    @Test
    public void saveAndRetrieveTask() {
        repo.save(USER);

        repo.save(TASK_1);

        Task retrieved = repo.findById(TASK_1.getId()).get();

        assertSameTask(TASK_1, retrieved);
    }

    @Test
    public void saveAndRetrieveMany() {
        repo.save(USER);

        repo.save(TASK_1);
        repo.save(TASK_2);

        Collection<Task> retrieved = repo.findAll();

        assertEquals(2, retrieved.size());
    }

    @Test
    public void taskNotFound_returnsNull() {
        Optional<Task> retrieved = repo.findById(TASK_1.getId());

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
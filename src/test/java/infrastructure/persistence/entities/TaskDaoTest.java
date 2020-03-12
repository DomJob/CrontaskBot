package infrastructure.persistence.entities;

import static org.junit.Assert.*;

import domain.schedule.Schedule;
import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import org.junit.Test;

public class TaskDaoTest {

    public static final Time TIME = new Time(123456);

    @Test
    public void fromModel_correctValues() {
        User user = new User(2, Timezone.fromOffset(60));
        String schedule = "1 2 3 4 5";
        Task task = new Task(1, "Hello", user, Schedule.parse(schedule, TIME, user.getTimezone()));

        TaskDao dao = TaskDao.fromModel(task);

        assertEquals(1, dao.id);
        assertEquals("Hello", dao.name);
        assertEquals(2, dao.owner.id);
        assertEquals(60, dao.owner.tzOffset);
        assertEquals(schedule, dao.schedule);
    }
}
package infrastructure.persistence.entities;

import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskId;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.Language;
import domain.user.User;
import domain.user.UserId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskDaoTest {

    public static final Time TIME = new Time(123456);
    public static final Time SNOOZED_UNTIL = TIME.plusMinutes(35);

    @Test
    public void fromModel_correctValues() {
        User user = new User(new UserId(2), Timezone.fromOffset(60), Language.ENGLISH);
        String schedule = "1 2 3 4 5";
        Task task = new Task(new TaskId(1), "Hello", user, Schedule.parse(schedule, TIME.withTimezone(user.getTimezone())));

        task.snoozeUntil(SNOOZED_UNTIL);

        TaskDao dao = TaskDao.fromModel(task);

        assertEquals(1, dao.id);
        assertEquals("Hello", dao.name);
        assertEquals(2, dao.owner.id);
        assertEquals(60, dao.owner.tzOffset);
        assertEquals(schedule, dao.schedule);
        assertEquals(SNOOZED_UNTIL.getMinutesSinceEpoch(), dao.snoozedUntil);
    }
}
package domain;

import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskId;
import domain.time.Time;
import domain.user.User;
import domain.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskTest {

    private static final Time NOW = Time.fromDate(2020, 3, 15, 20, 18);
    @Mock
    private Schedule schedule;
    private Task task;

    @Before
    public void setUp() {
        task = new Task(new TaskId(0), "Name", new User(new UserId(0)), schedule);
        when(schedule.isTriggered(any(Time.class))).thenReturn(false);
    }

    @Test
    public void isTriggered_checkGivenTime() {
        task.isTriggered(NOW);

        verify(schedule).isTriggered(NOW);
    }

    @Test
    public void nextTrigger_whenScheduleTriggersIn5minutes_then5minutes() {
        Time expectedNextTrigger = NOW.plusMinutes(5);
        when(schedule.nextTrigger(NOW)).thenReturn(expectedNextTrigger);

        Time nextTrigger = task.scheduledFor(NOW);

        assertEquals(expectedNextTrigger, nextTrigger);
    }

    @Test
    public void nextTrigger_whenScheduleTriggerIsInPast_thenNever() {
        when(schedule.nextTrigger(NOW)).thenReturn(NOW.plusMinutes(-5));

        Time nextTrigger = task.scheduledFor(NOW);

        assertEquals(Time.NEVER, nextTrigger);
    }

    @Test
    public void nextTrigger_whenScheduleTriggerIsInPast_butTaskSnoozed_thenSnoozedTime() {
        when(schedule.nextTrigger(NOW)).thenReturn(NOW.plusMinutes(-5));

        Time snoozedUntil = NOW.plusMinutes(15);
        task.snoozeUntil(snoozedUntil);

        Time nextTrigger = task.scheduledFor(NOW);

        assertEquals(snoozedUntil, nextTrigger);
    }

    @Test
    public void nextTrigger_whenScheduleIsSoon_butTaskSnoozedUntilLater_thenScheduleTime() {
        Time snoozedUntil = NOW.plusMinutes(15);
        Time scheduleTrigger = NOW.plusMinutes(5);
        when(schedule.nextTrigger(NOW)).thenReturn(scheduleTrigger);

        task.snoozeUntil(snoozedUntil);

        Time nextTrigger = task.scheduledFor(NOW);

        assertEquals(scheduleTrigger, nextTrigger);
    }

    @Test
    public void nextTrigger_whenTaskSnoozedForSoon_andScheduleTriggersLater_thenSnoozeTime() {
        Time snoozedUntil = NOW.plusMinutes(15);
        Time scheduleTrigger = NOW.plusMinutes(35);
        when(schedule.nextTrigger(NOW)).thenReturn(scheduleTrigger);

        task.snoozeUntil(snoozedUntil);

        Time nextTrigger = task.scheduledFor(NOW);

        assertEquals(snoozedUntil, nextTrigger);
    }

    @Test
    public void nextTrigger_whenTaskSnoozedTimePassed_andScheduleAlreadyTriggeredToo_thenNever() {
        Time snoozedUntil = NOW.plusMinutes(-15);
        Time scheduleTrigger = NOW.plusMinutes(-35);
        when(schedule.nextTrigger(NOW)).thenReturn(scheduleTrigger);

        task.snoozeUntil(snoozedUntil);

        Time nextTrigger = task.scheduledFor(NOW);

        assertEquals(Time.NEVER, nextTrigger);
    }
}
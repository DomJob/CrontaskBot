package domain;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import domain.Task.Task;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskTest {

    @Mock
    private Schedule schedule;

    private Task task;

    @Before
    public void setUp() {
        task = new Task(0, "Name", new User(0), schedule);
        when(schedule.isTriggered(any(Time.class), eq(Timezone.UTC))).thenReturn(false);
    }

    @Test
    public void isTriggered_checkGivenTime() {
        Time checkTime = Time.now().nextMinute();

        task.isTriggered(checkTime);

        verify(schedule).isTriggered(checkTime, Timezone.UTC);
    }
}
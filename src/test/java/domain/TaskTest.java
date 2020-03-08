package domain;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        task = new Task(0, "Name", 0, schedule, false);
        when(schedule.isTriggered(any(Time.class))).thenReturn(false);
    }

    @Test
    public void isTriggered_checkGivenTime() {
        Time checkTime = Time.now().nextMinute();

        task.isTriggered(checkTime);

        verify(schedule).isTriggered(checkTime);
    }
}
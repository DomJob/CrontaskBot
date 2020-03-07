package domain.cronschedule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.Schedule;
import domain.Time;
import org.junit.Test;

public class CronScheduleTest {
    @Test(expected = InvalidCronFormatException.class)
    public void parseCron_tooManyParams_ThrowsException() {
        CronSchedule.parse("* * * * * *");
    }

    @Test(expected = InvalidCronFormatException.class)
    public void parseCron_notEnoughParams_ThrowsException() {
        CronSchedule.parse("* * * *");
    }

    @Test
    public void allAnyMatchers_matchAnyDate() {
        Schedule schedule = CronSchedule.parse("* * * * *");

        assertTrue(schedule.isTriggered(createTime(27, 16, 7, 3, 6)));
    }

    @Test
    public void specificHourMatcher_matchThatHour() {
        Schedule schedule = CronSchedule.parse("* 16 * * *");

        assertTrue(schedule.isTriggered(createTime(27, 16, 7, 3, 6)));
    }

    @Test
    public void specificHourMatcher_doesntMatchOtherHours() {
        Schedule schedule = CronSchedule.parse("* 15 * * *");

        assertFalse(schedule.isTriggered(createTime(27, 16, 7, 3, 6)));
    }

    public static Time createTime(int minute, int hour, int day, int month, int weekday) {
        Time time = mock(Time.class);

        when(time.minute()).thenReturn(minute);
        when(time.hour()).thenReturn(hour);
        when(time.day()).thenReturn(day);
        when(time.month()).thenReturn(month);
        when(time.weekday()).thenReturn(weekday);

        return time;
    }
}
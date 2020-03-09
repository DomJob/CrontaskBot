package domain.schedule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import domain.time.Time;
import domain.time.Timezone;
import org.junit.Test;

public class CronScheduleTest {
    @Test(expected = InvalidScheduleException.class)
    public void parseCron_tooManyParams_ThrowsException() {
        CronSchedule.parse("* * * * * *");
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseCron_notEnoughParams_ThrowsException() {
        CronSchedule.parse("* * * *");
    }

    @Test
    public void allAnyMatchers_matchAnyDate() {
        Schedule schedule = CronSchedule.parse("* * * * *");

        assertTrue(schedule.isTriggered(Time.fromDate(2020,3,15,15,20)));
    }

    @Test
    public void specificHourMatcher_matchThatHour() {
        Schedule schedule = CronSchedule.parse("* 16 * * *");

        assertTrue(schedule.isTriggered(Time.fromDate(2020,3,15,16,20)));
    }

    @Test
    public void specificHourMatcher_doesntMatchOtherHours() {
        Schedule schedule = CronSchedule.parse("* 15 * * *");

        assertFalse(schedule.isTriggered(Time.fromDate(2020,3,15,16,20)));
    }
}
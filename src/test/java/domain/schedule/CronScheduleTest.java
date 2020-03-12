package domain.schedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import domain.time.Time;
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

        assertTrue(schedule.isTriggered(Time.fromDate(2020, 3, 15, 15, 20)));
    }

    @Test
    public void specificHourMatcher_matchThatHour() {
        Schedule schedule = CronSchedule.parse("* 16 * * *");

        assertTrue(schedule.isTriggered(Time.fromDate(2020, 3, 15, 16, 20)));
    }

    @Test
    public void specificHourMatcher_doesntMatchOtherHours() {
        Schedule schedule = CronSchedule.parse("* 15 * * *");

        assertFalse(schedule.isTriggered(Time.fromDate(2020, 3, 15, 16, 20)));
    }

    @Test
    public void nextTrigger_testMinute() {
        Time currentTime = Time.fromDate(2020, 3, 1, 16, 15);
        Time nextTrigger = Time.fromDate(2020, 3, 1, 16, 35);

        Time actualTime = getNextTrigger("35 * * * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testMinuteRollOver() {
        Time currentTime = Time.fromDate(2020, 3, 1, 16, 35);
        Time nextTrigger = Time.fromDate(2020, 3, 1, 17, 15);

        Time actualTime = getNextTrigger("15 * * * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testHour() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 3, 1, 16, 0);

        Time actualTime = getNextTrigger("* 16 * * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testDay() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 3, 15, 0, 0);

        Time actualTime = getNextTrigger("* * 15 * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testDayRollOver() {
        Time currentTime = Time.fromDate(2020, 3, 16, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 4, 15, 0, 0);

        Time actualTime = getNextTrigger("* * 15 * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testMonth() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 4, 1, 0, 0);

        Time actualTime = getNextTrigger("* * * 4 *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testDayOfWeek() {
        Time currentTime = Time.fromDate(2020, 3, 12, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 3, 17, 0, 0);

        Time actualTime = getNextTrigger("* * * * 2", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testMonthRollover() {
        Time currentTime = Time.fromDate(2020, 5, 2, 5, 20);
        Time nextTrigger = Time.fromDate(2021, 4, 1, 0, 0);

        Time actualTime = getNextTrigger("* * * 4 *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testHour_rollOver() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 3, 2, 4, 0);

        Time actualTime = getNextTrigger("* 4 * * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testDayOfMonth() {
        Time currentTime = Time.fromDate(2020, 3, 1, 16, 20);
        Time nextTrigger = Time.fromDate(2020, 3, 15, 0, 0);

        Time actualTime = getNextTrigger("* * 15 * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_testHourAndMinute() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 20);
        Time nextTrigger = Time.fromDate(2020, 3, 1, 16, 12);

        Time actualTime = getNextTrigger("12 16 * * *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_literallyMissedItByAMinuteLol() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 15);
        Time nextTrigger = Time.fromDate(2021, 3, 1, 5, 14);

        Time actualTime = getNextTrigger("14 5 1 3 *", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    @Test
    public void nextTrigger_literallyMissedItByAMinuteButAlsoItHasToBeASpecificWeekday() {
        Time currentTime = Time.fromDate(2020, 3, 1, 5, 15);
        Time nextTrigger = Time.fromDate(2026, 3, 1, 5, 14); // The next Sunday March 1st is in 2026

        Time actualTime = getNextTrigger("14 5 1 3 0", currentTime);

        assertEquals(nextTrigger, actualTime);
    }

    private Time getNextTrigger(String scheduleStr, Time now) {
        CronSchedule schedule = CronSchedule.parse(scheduleStr);
        return schedule.nextTrigger(now);
    }
}
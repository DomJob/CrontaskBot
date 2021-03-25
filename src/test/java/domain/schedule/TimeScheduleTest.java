package domain.schedule;

import domain.time.Time;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TimeScheduleTest {
    public static final Time NOW = Time.fromDate(2020, 3, 1, 15, 20);

    @Test
    public void parseFullTime() {
        Time time = NOW.plusMinutes(30);
        String inputString = String.format("%d-%d-%d %d:%d", time.year(), time.month(), time.day(), time.hour(), time.minute());

        Schedule schedule = TimeSchedule.parseAbsoluteTime(inputString, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseFullTimeWithoutYear() {
        Time time = NOW.plusMinutes(30);
        String inputString = String.format("%d/%d %d:%d", time.month(), time.day(), time.hour(), time.minute());

        Schedule schedule = TimeSchedule.parseAbsoluteTime(inputString, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseTimeHourAndMinute() {
        Time time = NOW.plusMinutes(30);
        String inputString = String.format("%d:%d", time.hour(), time.minute());

        Schedule schedule = TimeSchedule.parseAbsoluteTime(inputString, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseFullTime_inPast_throwsException() {
        Time time = NOW.plusMonths(-1);

        String inputString = String.format("%d-%d-%d %d:%d", time.year(), time.month(), time.day(), time.hour(), time.minute());

        TimeSchedule.parseAbsoluteTime(inputString, NOW);
    }

    @Test
    public void parseTimeHourAndMinute_rollsOverToNextDay_ifTimeAlreadyPassed() {
        Time time = NOW.plusHours(-1);
        Time expectedTriggerTime = NOW.plusHours(23);

        String inputString = String.format("%d:%d", time.hour(), time.minute());

        Schedule schedule = TimeSchedule.parseAbsoluteTime(inputString, NOW);

        assertTrue(schedule.isTriggered(expectedTriggerTime));
    }

    @Test
    public void parseTimeMonthDayHourMinute_rollsOverToNextYear_ifTimereadyPassed() {
        Time time = NOW.plusMonths(-1);
        Time expectedTriggerTime = NOW.plusMonths(11);

        String inputString = String.format("%d-%d %d:%d", time.month(), time.day(), time.hour(), time.minute());

        Schedule schedule = TimeSchedule.parseAbsoluteTime(inputString, NOW);

        assertTrue(schedule.isTriggered(expectedTriggerTime));
    }

    @Test
    public void parseTimeYearMonthDay_setsForMidnightOfThatDay() {
        Time expectedTriggerTime = Time.fromDate(2020, 3, 14, 0, 0);

        String inputString = "2020-03-14";

        Schedule schedule = TimeSchedule.parseAbsoluteTime(inputString, NOW);

        assertTrue(schedule.isTriggered(expectedTriggerTime));
    }

    @Test
    public void parseRelativeTime_minutesFromNow() {
        Time time = NOW.plusMinutes(23);

        String string = "23 minutes";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseRelativeTime_hoursFromNow() {
        Time time = NOW.plusHours(23);

        String string = "in 23 hours";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseRelativeTime_daysFromNow() {
        Time time = NOW.plusDays(5);

        String string = "5 days from now";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseRelativeTime_monthsFromNow() {
        Time time = NOW.plusMonths(5);

        String string = "5 months";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseRelativeTime_yearsFromNow() {
        Time time = NOW.plusYears(5);

        String string = "5 years";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseRelativeTime_complexThingy() {
        Time time = NOW.plusYears(5).plusMonths(2).plusDays(5).plusHours(2).plusMinutes(1);

        String string = "5 years, 2 months, 5 days, 2 hours and 1 minute from now";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test
    public void parseRelativeTime_complexThingy_2() {
        Time time = NOW.plusYears(5).plusMonths(2).plusHours(2).plusMinutes(3);

        String string = "5 years, 2 months, 2 hours and 3 minutes from now";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);

        assertTrue(schedule.isTriggered(time));
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseSchedule_invalidFormat() {
        String string = "literally anything that breaks this";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseSchedule_0minutesFromNow_throwsException() {
        String string = "0 minutes from now";

        Schedule schedule = TimeSchedule.parseRelativeTime(string, NOW);
    }
}
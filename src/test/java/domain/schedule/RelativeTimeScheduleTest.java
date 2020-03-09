package domain.schedule;

import static org.junit.Assert.assertTrue;

import domain.time.Time;
import domain.time.Timezone;
import org.junit.Test;

public class RelativeTimeScheduleTest {

    private static Time NOW = Time.fromDate(2020, 3, 15, 15, 20);

    @Test
    public void parseRelativeTime_minutesFromNow() {
        Time time = NOW.plusMinutes(23);

        String string = "23 minutes";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_hoursFromNow() {
        Time time = NOW.plusHours(23);

        String string = "in 23 hours";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_daysFromNow() {
        Time time = NOW.plusDays(5);

        String string = "5 days from now";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_monthsFromNow() {
        Time time = NOW.plusMonths(5);

        String string = "5 months";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_yearsFromNow() {
        Time time = NOW.plusYears(5);

        String string = "5 years";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_complexThingy() {
        Time time = NOW.plusYears(5).plusMonths(2).plusDays(5).plusHours(2).plusMinutes(1);

        String string = "5 years, 2 months, 5 days, 2 hours and 1 minute from now";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_complexThingy_2() {
        Time time = NOW.plusYears(5).plusMonths(2).plusHours(2).plusMinutes(3);

        String string = "5 years, 2 months, 2 hours and 3 minutes from now";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseSchedule_invalidFormat() {
        String string = "literally anything that breaks this";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseSchedule_0minutesFromNow() {
        String string = "0 minutes from now";

        Schedule schedule = RelativeTimeSchedule.parse(string, NOW);
    }
}
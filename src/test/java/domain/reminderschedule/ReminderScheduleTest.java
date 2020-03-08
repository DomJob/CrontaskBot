package domain.reminderschedule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import domain.Schedule;
import domain.time.Time;
import domain.time.Timezone;
import org.junit.Test;

public class ReminderScheduleTest {

    @Test
    public void minutesFromNow_triggersAtCorrectTime() {
        int m = 5;

        Schedule schedule = ReminderSchedule.minutesFromNow(m);

        Time now = Time.now();

        for (int i = 1; i <= m; i++) {
            assertFalse(schedule.isTriggered(now, Timezone.UTC));
            now = now.nextMinute();
        }

        assertTrue(schedule.isTriggered(now, Timezone.UTC));
    }

    @Test
    public void parseFullTime() {
        Time time = Time.now().nextMinute();
        String inputString = String.format("%d-%d-%d %d:%d", time.year(), time.month(), time.day(), time.hour(), time.minute());

        Schedule schedule = ReminderSchedule.parse(inputString);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseTimeWithoutYear() {
        Time time = Time.now().nextMinute();
        String inputString = String.format("%d-%d %d:%d", time.month(), time.day(), time.hour(), time.minute());

        Schedule schedule = ReminderSchedule.parse(inputString);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseTimeHourAndMinute() {
        Time time = Time.now().nextMinute();
        String inputString = String.format("%d:%d", time.hour(), time.minute());

        Schedule schedule = ReminderSchedule.parse(inputString);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }
    
    @Test
    public void parseRelativeTime_minutesFromNow() {
        Time time = Time.now().plusMinutes(23);

        String string = "23 minutes";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_hoursFromNow() {
        Time time = Time.now().plusHours(23);

        String string = "in 23 hours";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_daysFromNow() {
        Time time = Time.now().plusDays(5);

        String string = "5 days from now";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_monthsFromNow() {
        Time time = Time.now().plusMonths(5);

        String string = "5 months";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_yearsFromNow() {
        Time time = Time.now().plusYears(5);

        String string = "5 years";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_complexThingy() {
        Time time = Time.now().plusYears(5).plusMonths(2).plusDays(5).plusHours(2).plusMinutes(1);

        String string = "5 years, 2 months, 5 days, 2 hours and 1 minute from now";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test
    public void parseRelativeTime_complexThingy_2() {
        Time time = Time.now().plusYears(5).plusMonths(2).plusHours(2).plusMinutes(3);

        String string = "5 years, 2 months, 2 hours and 3 minutes from now";

        Schedule schedule = ReminderSchedule.parse(string);

        assertTrue(schedule.isTriggered(time, Timezone.UTC));
    }

    @Test(expected = InvalidReminderFormatException.class)
    public void parseSchedule_invalidFormat() {
        String string = "literally anything that breaks this";

        Schedule schedule = ReminderSchedule.parse(string);
    }

    @Test(expected = InvalidReminderFormatException.class)
    public void parseSchedule_0minutesFromNow() {
        String string = "0 minutes from now";

        Schedule schedule = ReminderSchedule.parse(string);
    }
}
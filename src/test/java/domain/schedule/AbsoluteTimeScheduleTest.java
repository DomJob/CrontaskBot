package domain.schedule;

import static org.junit.Assert.assertTrue;

import domain.time.Time;
import domain.time.Timezone;
import org.junit.Test;

public class AbsoluteTimeScheduleTest {

    public static final Time NOW = Time.fromDate(2020,3,1,15,20);

    @Test
    public void parseFullTime() {
        String inputString = String.format("%d-%d-%d %d:%d", NOW.year(), NOW.month(), NOW.day(), NOW.hour(), NOW.minute());

        Schedule schedule = AbsoluteTimeSchedule.parse(inputString, NOW);

        assertTrue(schedule.isTriggered(NOW, Timezone.UTC));
    }

    @Test
    public void parseFullTimeWithoutYear() {
        String inputString = String.format("%d/%d %d:%d", NOW.month(), NOW.day(), NOW.hour(), NOW.minute());

        Schedule schedule = AbsoluteTimeSchedule.parse(inputString, NOW);

        assertTrue(schedule.isTriggered(NOW, Timezone.UTC));
    }

    @Test
    public void parseTimeHourAndMinute() {
        String inputString = String.format("%d:%d", NOW.hour(), NOW.minute());

        Schedule schedule = AbsoluteTimeSchedule.parse(inputString, NOW);

        assertTrue(schedule.isTriggered(NOW, Timezone.UTC));
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseFullTime_inPast_throwsException() {
        Time time = NOW.plusMonths(-1);

        String inputString = String.format("%d-%d-%d %d:%d", time.year(), time.month(), time.day(), time.hour(), time.minute());

        AbsoluteTimeSchedule.parse(inputString, NOW);
    }

    @Test
    public void parseTimeHourAndMinute_rollsOverToNextDay_ifTimereadyPassed() {
        Time time = NOW.plusHours(-1);
        Time expectedTriggerTime = NOW.plusHours(23);

        String inputString = String.format("%d:%d", time.hour(), time.minute());

        Schedule schedule = AbsoluteTimeSchedule.parse(inputString, NOW);

        assertTrue(schedule.isTriggered(expectedTriggerTime, Timezone.UTC));
    }

    @Test
    public void parseTimeMonthDayYear_rollsOverToNextYear_ifTimereadyPassed() {
        Time time = NOW.plusMonths(-1);
        Time expectedTriggerTime = NOW.plusMonths(11);

        String inputString = String.format("%d-%d %d:%d", time.month(), time.day(), time.hour(), time.minute());

        Schedule schedule = AbsoluteTimeSchedule.parse(inputString, NOW);

        assertTrue(schedule.isTriggered(expectedTriggerTime, Timezone.UTC));
    }

    @Test(expected = InvalidScheduleException.class)
    public void parseSchedule_invalidFormat() {
        String string = "literally anything that breaks this";

        AbsoluteTimeSchedule.parse(string, NOW);
    }
}
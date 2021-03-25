package domain.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimezoneTest {
    @Test
    public void parseSingleHour() {
        Timezone tz = Timezone.fromString("4");

        assertEquals(4 * 60, tz.getOffset());
    }

    @Test
    public void parseSingleHour_negative() {
        Timezone tz = Timezone.fromString("-4");

        assertEquals(-4 * 60, tz.getOffset());
    }

    @Test
    public void parseHourAndMinute() {
        Timezone tz = Timezone.fromString("4:30");

        assertEquals(4 * 60 + 30, tz.getOffset());
    }

    @Test
    public void parseHourAndMinute_negative() {
        Timezone tz = Timezone.fromString("-4:30");

        assertEquals(-4 * 60 - 30, tz.getOffset());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseAnythingElse_throwsException() {
        Timezone tz = Timezone.fromString("4asdsa2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseTooManyHours_throwsException() {
        Timezone tz = Timezone.fromString("15");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseTooManyMinutes_throwsException() {
        Timezone tz = Timezone.fromString("3:60");
    }
}
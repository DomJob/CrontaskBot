package domain.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimezoneTest {
    @Test
    public void parseSingleHour() {
        Timezone tz = Timezone.parse("4");

        assertEquals(4 * 60, tz.getOffset());
    }

    @Test
    public void parseSingleHour_negative() {
        Timezone tz = Timezone.parse("-4");

        assertEquals(-4 * 60, tz.getOffset());
    }

    @Test
    public void parseHourAndMinute() {
        Timezone tz = Timezone.parse("4:30");

        assertEquals(4 * 60 + 30, tz.getOffset());
    }

    @Test
    public void parseHourAndMinute_negative() {
        Timezone tz = Timezone.parse("-4:30");

        assertEquals(-4 * 60 - 30, tz.getOffset());
    }

    @Test(expected = InvalidTimezoneOffsetException.class)
    public void parseAnythingElse_throwsException() {
        Timezone tz = Timezone.parse("4asdsa2");
    }

    @Test(expected = InvalidTimezoneOffsetException.class)
    public void parseTooManyHours_throwsException() {
        Timezone tz = Timezone.parse("15");
    }

    @Test(expected = InvalidTimezoneOffsetException.class)
    public void parseTooManyMinutes_throwsException() {
        Timezone tz = Timezone.parse("3:60");
    }
}
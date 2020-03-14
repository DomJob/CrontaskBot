package domain.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class TimeTest {

    @Test
    public void fromDate_correctEpoch() {
        Time expected = new Time(26393270);

        Time actual = Time.fromDate(2020, 3, 7, 15, 50);

        assertEquals(expected, actual);
    }

    @Test
    public void fromDate_correctValues() {
        Time time = Time.fromDate(2020, 3, 7, 15, 50);

        assertEquals(2020, time.year());
        assertEquals(3, time.month());
        assertEquals(7, time.day());
        assertEquals(15, time.hour());
        assertEquals(50, time.minute());
    }

    @Test
    public void plusMinutes() {
        Time now = Time.fromDate(2020, 3, 7, 15, 50);

        Time later = now.plusMinutes(5);

        assertEquals(now.year(), later.year());
        assertEquals(now.month(), later.month());
        assertEquals(now.day(), later.day());
        assertEquals(now.hour(), later.hour());
        assertEquals(now.minute() + 5, later.minute());
    }

    @Test
    public void plusMinutes_overflow() {
        Time now = Time.fromDate(2020, 3, 7, 15, 50);

        Time later = now.plusMinutes(63);

        assertEquals(now.year(), later.year());
        assertEquals(now.month(), later.month());
        assertEquals(now.day(), later.day());
        assertEquals(now.hour() + 1, later.hour());
        assertEquals(now.minute() + 3, later.minute());
    }

    @Test
    public void plusHours() {
        Time now = Time.fromDate(2020, 3, 7, 15, 50);

        Time later = now.plusHours(5);

        assertEquals(now.year(), later.year());
        assertEquals(now.month(), later.month());
        assertEquals(now.day(), later.day());
        assertEquals(now.hour() + 5, later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void plusHours_overflow() {
        Time now = Time.fromDate(2020, 3, 7, 15, 50);

        Time later = now.plusHours(26);

        assertEquals(now.year(), later.year());
        assertEquals(now.month(), later.month());
        assertEquals(now.day() + 1, later.day());
        assertEquals(now.hour() + 2, later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void plusDays() {
        Time now = Time.fromDate(2020, 3, 7, 15, 50);

        Time later = now.plusDays(5);

        assertEquals(now.year(), later.year());
        assertEquals(now.month(), later.month());
        assertEquals(now.day() + 5, later.day());
        assertEquals(now.hour(), later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void plusDays_overflow() {
        Time now = Time.fromDate(2020, 2, 28, 15, 50);

        Time later = now.plusDays(5);

        assertEquals(now.year(), later.year());
        assertEquals(now.month() + 1, later.month());
        assertEquals(4, later.day());
        assertEquals(now.hour(), later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void plusMonths() {
        Time now = Time.fromDate(2020, 3, 7, 15, 50);

        Time later = now.plusMonths(5);

        assertEquals(now.year(), later.year());
        assertEquals(now.month() + 5, later.month());
        assertEquals(now.day(), later.day());
        assertEquals(now.hour(), later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void plusMonths_overflow() {
        Time now = Time.fromDate(2020, 2, 28, 15, 50);

        Time later = now.plusMonths(11);

        assertEquals(now.year() + 1, later.year());
        assertEquals(now.month() - 1, later.month());
        assertEquals(now.day(), later.day());
        assertEquals(now.hour(), later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void plusYears() {
        Time now = Time.fromDate(2020, 2, 28, 15, 50);

        Time later = now.plusYears(6);

        assertEquals(now.year() + 6, later.year());
        assertEquals(now.month(), later.month());
        assertEquals(now.day(), later.day());
        assertEquals(now.hour(), later.hour());
        assertEquals(now.minute(), later.minute());
    }

    @Test
    public void sortTimes() {
        Time earliest = Time.fromDate(2020,1,1,0,0);
        Time middle = Time.fromDate(2020,1,5,0,0);
        Time latest = Time.fromDate(2020,2,1,0,0);

        List<Time> list = Arrays.asList(middle, earliest, latest);

        list.sort(Time::compareTo);

        assertSame(earliest, list.get(0));
        assertSame(middle, list.get(1));
        assertSame(latest, list.get(2));
    }
}
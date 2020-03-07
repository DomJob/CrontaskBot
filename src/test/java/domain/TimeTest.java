package domain;

import static org.junit.Assert.*;

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
}
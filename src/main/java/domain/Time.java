package domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class Time {
    private long minutesSinceEpoch;
    private LocalDateTime time;

    public Time(long minutesSinceEpoch) {
        this.minutesSinceEpoch = minutesSinceEpoch;
        this.time = Instant.ofEpochSecond(minutesSinceEpoch*60).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public Time nextMinute() {
        return new Time(minutesSinceEpoch+1);
    }

    public static Time now() {
        long secondsSinceEpoch = Instant.now().getEpochSecond();

        long minutes = secondsSinceEpoch / 60;

        return new Time(minutes);
    }

    public static Time fromDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month-1, day, hour, minute, 0);

        return new Time(calendar.getTimeInMillis() / (60*1000));
    }

    public int year() {
        return time.getYear();
    }

    public int month() {
        return time.getMonth().getValue();
    }

    public int day() {
        return time.getDayOfMonth();
    }

    public int hour() {
        return time.getHour();
    }

    public int minute() {
        return time.getMinute();
    }

    public int weekday() {
        return time.getDayOfWeek().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Time time = (Time) o;
        return minutesSinceEpoch == time.minutesSinceEpoch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutesSinceEpoch);
    }

    @Override
    public String toString() {
        return "Time{" +
            "minutesSinceEpoch=" + minutesSinceEpoch +
            '}';
    }
}

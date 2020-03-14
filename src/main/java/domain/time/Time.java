package domain.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import org.jetbrains.annotations.NotNull;

public class Time implements Comparable<Time> {
    public static final Time NEVER = new Time(0);

    private long minutesSinceEpoch;
    private LocalDateTime time;

    public Time(long minutesSinceEpoch) {
        this.minutesSinceEpoch = minutesSinceEpoch;
        this.time = Instant.ofEpochSecond(minutesSinceEpoch * 60).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static Time fromDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month - 1, day, hour, minute, 0);

        return new Time(calendar.getTimeInMillis() / (60 * 1000));
    }

    public static Time fromLocalDateTime(LocalDateTime time) {
        return new Time(time.toEpochSecond(ZoneOffset.UTC) / 60);
    }

    public static Time max(Time t1, Time t2) {
        return t1.isAfter(t2) ? t1 : t2;
    }

    public static Time min(Time t1, Time t2) {
        return t2.isAfter(t1) ? t1 : t2;
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
        return time.getDayOfWeek().getValue() % 7;
    }

    public Time plusMinutes(int minutes) {
        return new Time(minutesSinceEpoch + minutes);
    }

    public Time plusHours(int hours) {
        return plusMinutes(hours * 60);
    }

    public Time plusDays(int days) {
        return fromLocalDateTime(time.plusDays(days));
    }

    public Time plusMonths(int months) {
        return fromLocalDateTime(time.plusMonths(months));
    }

    public Time plusYears(int years) {
        return fromLocalDateTime(time.plusYears(years));
    }

    public Time withTimezone(Timezone timezone) {
        return plusMinutes(timezone.getOffset());
    }

    public boolean isAfter(Time other) {
        return minutesSinceEpoch > other.minutesSinceEpoch;
    }

    public boolean isBefore(Time other) {
        return minutesSinceEpoch < other.minutesSinceEpoch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutesSinceEpoch);
    }

    public long getMinutesSinceEpoch() {
        return minutesSinceEpoch;
    }

    @Override
    public int compareTo(@NotNull Time other) {
        return Long.compare(minutesSinceEpoch, other.minutesSinceEpoch);
    }

    @Override
    public String toString() {
        return String.format("%d-%02d-%02d %02d:%02d", year(), month(), day(), hour(), minute());
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
}

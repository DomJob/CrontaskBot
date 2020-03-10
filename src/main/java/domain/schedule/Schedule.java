package domain.schedule;

import domain.time.Time;
import domain.time.Timezone;

public interface Schedule {
    static Schedule parse(String string, Time now, Timezone timezone) {
        string = string.toLowerCase();

        if (string.matches(TimeSchedule.ABSOLUTE_PATTERN)) {
            return TimeSchedule.parseAbsoluteTime(string, now);
        } else if (string.matches(TimeSchedule.RELATIVE_PATTERN)) {
            return TimeSchedule.parseRelativeTime(string, now.withTimezone(timezone));
        } else if (string.matches(CronSchedule.PATTERN)) {
            return CronSchedule.parse(string);
        }

        throw new InvalidScheduleException();
    }

    static Schedule deserialize(String string) {
        if (string.matches(CronSchedule.PATTERN)) {
            return CronSchedule.parse(string);
        } else if (string.matches("^\\d+$")) {
            return new TimeSchedule(new Time(Integer.parseInt(string)));
        }

        throw new InvalidScheduleException();
    }

    boolean isTriggered(Time time);

    Time nextTrigger(Time now);

    String serialize();
}

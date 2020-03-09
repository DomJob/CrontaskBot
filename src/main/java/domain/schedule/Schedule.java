package domain.schedule;

import domain.time.Time;
import domain.time.Timezone;

public interface Schedule {
    boolean isTriggered(Time time);

    Time nextTrigger(Time now);

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
}

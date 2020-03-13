package domain.schedule;

import domain.time.Time;

public abstract class Schedule {
    protected String code;

    public static Schedule parse(String string, Time now) {
        string = string.toLowerCase();

        if (string.matches(TimeSchedule.ABSOLUTE_PATTERN)) {
            return TimeSchedule.parseAbsoluteTime(string, now);
        } else if (string.matches(TimeSchedule.RELATIVE_PATTERN)) {
            return TimeSchedule.parseRelativeTime(string, now);
        } else if (string.matches(CronSchedule.PATTERN)) {
            return CronSchedule.parse(string);
        }

        throw new InvalidScheduleException();
    }

    public static Schedule deserialize(String string) {
        if (string.matches(CronSchedule.PATTERN)) {
            return CronSchedule.parse(string);
        } else if (string.matches("^\\d+$")) {
            long time = Long.parseLong(string);
            return new TimeSchedule(new Time(time));
        }

        throw new InvalidScheduleException();
    }

    public abstract boolean isTriggered(Time time);

    public abstract Time nextTrigger(Time now);

    @Override
    public String toString() {
        return code;
    }
}

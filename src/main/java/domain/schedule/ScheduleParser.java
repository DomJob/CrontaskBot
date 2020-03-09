package domain.schedule;

import domain.time.Time;

public class ScheduleParser {
    public Schedule parse(String string, Time now) {
        string = string.toLowerCase();

        if (string.matches(AbsoluteTimeSchedule.PATTERN)) {
            return AbsoluteTimeSchedule.parse(string, now);
        } else if (string.matches(RelativeTimeSchedule.PATTERN)) {
            return RelativeTimeSchedule.parse(string, now);
        } else if (string.matches(CronSchedule.PATTERN)) {
            return CronSchedule.parse(string);
        }

        throw new InvalidScheduleException();
    }
}

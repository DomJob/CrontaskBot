package domain.cronschedule;

import domain.Schedule;
import domain.time.Time;
import domain.time.Timezone;

public class CronSchedule implements Schedule {
    private String code;

    private CronMatcher minute;
    private CronMatcher hour;
    private CronMatcher day;
    private CronMatcher month;
    private CronMatcher weekday;

    public CronSchedule(CronMatcher minute, CronMatcher hour, CronMatcher day, CronMatcher month, CronMatcher weekday) {
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.weekday = weekday;
    }

    @Override
    public boolean isTriggered(Time time, Timezone timezone) {
        time = time.withTimezone(timezone);
        return minute.match(time.minute())
            && hour.match(time.hour())
            && day.match(time.day())
            && month.match(time.month())
            && weekday.match(time.weekday());
    }

    public static CronSchedule parse(String string) {
        String[] matchers = string.split(" ");

        if(matchers.length != 5) {
            throw new InvalidCronFormatException();
        }

        CronSchedule schedule = new CronSchedule(
            CronMatcher.parse(matchers[0]),
            CronMatcher.parse(matchers[1]),
            CronMatcher.parse(matchers[2]),
            CronMatcher.parse(matchers[3]),
            CronMatcher.parse(matchers[4])
        );

        schedule.code = string;

        return schedule;
    }

    @Override
    public String toString() {
        return code;
    }
}

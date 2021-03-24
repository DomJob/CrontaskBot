package domain.schedule;

import static infrastructure.util.Helper.extractNumbers;

import domain.schedule.cronmatchers.CronMatcher;
import domain.time.Time;

public class CronSchedule extends Schedule {
    protected static String PATTERN = "^((?:[\\s]|^)(\\*(/[0-9]+)?|([0-9]+-[0-9]+)|([0-9]+,)*[0-9]+)(?=[\\s]|$)){5}$";

    private final CronMatcher minute;
    private final CronMatcher hour;
    private final CronMatcher day;
    private final CronMatcher month;
    private final CronMatcher weekday;

    public CronSchedule(CronMatcher minute, CronMatcher hour, CronMatcher day, CronMatcher month, CronMatcher weekday) {
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.weekday = weekday;
    }

    public static CronSchedule parse(String string) {
        if (!string.matches(PATTERN)) {
            throw new InvalidScheduleException();
        }

        String[] matchers = string.split(" ");

        validateMatchers(matchers);

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

    private static void validateMatchers(String[] matchers) {
        if (matchers.length != 5) {
            throw new InvalidScheduleException();
        }
        int[] maxDaysInMonth = new int[] {-1, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        for (int minuteNumber : extractNumbers(matchers[0])) {
            if (minuteNumber >= 60) {
                throw new InvalidScheduleException();
            }
        }
        for (int hourNumber : extractNumbers(matchers[1])) {
            if (hourNumber >= 24) {
                throw new InvalidScheduleException();
            }
        }
        for (int dayNumber : extractNumbers(matchers[2])) {
            if (dayNumber == 0 || dayNumber >= 32) {
                throw new InvalidScheduleException();
            }
        }
        for (int monthNumber : extractNumbers(matchers[3])) {
            if (monthNumber == 0 || monthNumber > 12) {
                throw new InvalidScheduleException();
            }

            for (int dayNumber : extractNumbers(matchers[2])) {
                if (dayNumber > maxDaysInMonth[monthNumber]) {
                    throw new InvalidScheduleException();
                }
            }
        }
        for (int weekdayNumber : extractNumbers(matchers[4])) {
            if (weekdayNumber > 6) {
                throw new InvalidScheduleException();
            }
        }
    }

    @Override
    public boolean isTriggered(Time time) {
        return minute.match(time.minute())
            && hour.match(time.hour())
            && day.match(time.day())
            && month.match(time.month())
            && weekday.match(time.weekday());
    }

    @Override
    public Time nextTrigger(Time now) {
        Time st = now.plusMinutes(1);

        while (true) {
            if (!month.match(st.month())) {
                st = st.plusMonths(1);
                st = Time.fromDate(st.year(), st.month(), 1, 0, 0);
                continue;
            }
            if (!day.match(st.day()) || !weekday.match(st.weekday())) {
                st = st.plusDays(1);
                st = Time.fromDate(st.year(), st.month(), st.day(), 0, 0);
                continue;
            }
            if (!hour.match(st.hour())) {
                st = st.plusHours(1);
                st = Time.fromDate(st.year(), st.month(), st.day(), st.hour(), 0);
                continue;
            }
            if (!minute.match(st.minute())) {
                st = st.plusMinutes(1);
                continue;
            }

            break;
        }

        return st;
    }
}

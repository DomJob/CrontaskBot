package domain.schedule;

import static infrastructure.util.Helper.extractNumbers;

import domain.time.Time;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeSchedule extends Schedule {
    protected static final String ABSOLUTE_PATTERN = "^(([0-9]{4}[-\\/])?[0-9]{1,2}[-\\/][0-9]{1,2})?(((?<=[\\s\\S])\\s(?=[\\s\\S]))|((?<![\\s\\S])|(?![\\s\\S])))([0-9]{1,2}[\\:h][0-9]{1,2})?$";
    protected static String RELATIVE_PATTERN = "^(in\\s)?(([0-9]+)\\s(days?|months?|years?|hours?|minutes?)(,|\\sand\\s)?\\s?)+(from\\snow)?$";

    private static String YYYYMMDD_HHMM = "^[0-9]{4}[\\/-][0-9]{1,2}[\\/-][0-9]{1,2}\\s[0-9]{1,2}[\\:h][0-9]{1,2}$";
    private static String YYYYMMDD = "^[0-9]{4}[\\/-][0-9]{1,2}[\\/-][0-9]{1,2}$";
    private static String MMDD_HHMM = "^[0-9]{1,2}[\\/-][0-9]{1,2}\\s[0-9]{1,2}[\\:h][0-9]{1,2}$";
    private static String HHMM = "^[0-9]{1,2}[\\:h][0-9]{1,2}$";
    private static String MMDD = "^[0-9]{1,2}[\\/-][0-9]{1,2}$";

    private Time time;

    public TimeSchedule(Time time) {
        this.time = time;
        this.code = Long.toString(time.getMinutesSinceEpoch());
    }

    public static TimeSchedule parseAbsoluteTime(String string, Time now) {
        string = string.toLowerCase();
        if (!string.matches(ABSOLUTE_PATTERN)) {
            throw new InvalidScheduleException();
        }

        List<Integer> values = extractNumbers(string);

        int year = now.year();
        int month = now.month();
        int day = now.day();
        int hour = now.hour();
        int minute = now.minute();

        Time time = now;

        if(string.matches(YYYYMMDD_HHMM)) {
            year = values.get(0);
            month = values.get(1);
            day = values.get(2);
            hour = values.get(3);
            minute = values.get(4);
            time = Time.fromDate(year, month, day, hour, minute);

        } else if (string.matches(YYYYMMDD)) {
            year = values.get(0);
            month = values.get(1);
            day = values.get(2);
            hour = 0;
            minute = 0;

            time = Time.fromDate(year, month, day, hour, minute);
        } else if (string.matches(MMDD)) {
            month = values.get(0);
            day = values.get(1);
            hour = 0;
            minute = 0;

            time = Time.fromDate(year, month, day, hour, minute);
            if(time.isBefore(now)) {
                time = time.plusYears(1);
            }
        } else if (string.matches(MMDD_HHMM)) {
            month = values.get(0);
            day = values.get(1);
            hour = values.get(2);
            minute = values.get(3);

            time = Time.fromDate(year, month, day, hour, minute);
            if(time.isBefore(now)) {
                time = time.plusYears(1);
            }
        } else if (string.matches(HHMM)) {
            hour = values.get(0);
            minute = values.get(1);

            time = Time.fromDate(year, month, day, hour, minute);
            if(time.isBefore(now)) {
                time = time.plusDays(1);
            }
        }

        if(!time.isAfter(now)) {
            throw new InvalidScheduleException();
        }

        return new TimeSchedule(time);
    }

    public static TimeSchedule parseRelativeTime(String string, Time now) {
        string = string.toLowerCase();
        if (!string.matches(RELATIVE_PATTERN)) {
            throw new InvalidScheduleException();
        }

        Time scheduledTime = now;

        Pattern p = Pattern.compile("([0-9]+)\\s(years?|months?|days?|hours?|minutes?)");
        Matcher m = p.matcher(string);

        while (m.find()) {
            int value = Integer.parseInt(m.group(1));

            switch (m.group(2)) {
                case "years":
                case "year":
                    scheduledTime = scheduledTime.plusYears(value);
                    break;
                case "months":
                case "month":
                    scheduledTime = scheduledTime.plusMonths(value);
                    break;
                case "days":
                case "day":
                    scheduledTime = scheduledTime.plusDays(value);
                    break;
                case "hours":
                case "hour":
                    scheduledTime = scheduledTime.plusHours(value);
                    break;
                case "minutes":
                case "minute":
                    scheduledTime = scheduledTime.plusMinutes(value);
                    break;
            }
        }

        if (scheduledTime.equals(now)) {
            throw new InvalidScheduleException();
        }

        return new TimeSchedule(scheduledTime);
    }

    @Override
    public boolean isTriggered(Time time) {
        return this.time.equals(time);
    }

    @Override
    public Time nextTrigger(Time now) {
        return time;
    }
}

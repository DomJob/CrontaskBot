package domain.schedule;

import static infrastructure.util.Helper.extractNumbers;

import domain.time.Time;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeSchedule implements Schedule {
    protected static final String ABSOLUTE_PATTERN = "^(([0-9]{4}[\\-\\s/])?[0-9]{1,2}[\\-\\s/][0-9]{1,2}\\s)?[0-9]{1,2}[:h][0-9]{1,2}$";
    protected static String RELATIVE_PATTERN = "^(in\\s)?(([0-9]+)\\s(days?|months?|years?|hours?|minutes?)(,|\\sand\\s)?\\s?)+(from\\snow)?$";

    private Time time;

    public TimeSchedule(Time time) {
        this.time = time;
    }

    public static TimeSchedule parseAbsoluteTime(String string, Time now) {
        string = string.toLowerCase();
        if (!string.matches(ABSOLUTE_PATTERN)) {
            throw new InvalidScheduleException();
        }

        List<Integer> values = extractNumbers(string);
        Collections.reverse(values);

        int year = now.year();
        int month = now.month();
        int day = now.day();
        int hour = now.hour();
        int minute = now.minute();

        int nbNumbers = values.size();
        switch (nbNumbers) {
            case 5:
                year = values.get(4);
            case 4:
                month = values.get(3);
                day = values.get(2);
            case 2:
                hour = values.get(1);
                minute = values.get(0);
        }

        Time scheduledTime = Time.fromDate(year, month, day, hour, minute);

        if (now.isAfter(scheduledTime)) {
            if (nbNumbers == 2) {
                scheduledTime = scheduledTime.plusDays(1);
            } else if (nbNumbers == 4) {
                scheduledTime = scheduledTime.plusYears(1);
            } else {
                throw new InvalidScheduleException();
            }
        }

        return new TimeSchedule(scheduledTime);
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
        if(time.isAfter(now)) {
            return time;
        } else {
            return Time.NEVER;
        }
    }

    @Override
    public String serialize() {
        return Long.toString(time.getMinutesSinceEpoch());
    }
}

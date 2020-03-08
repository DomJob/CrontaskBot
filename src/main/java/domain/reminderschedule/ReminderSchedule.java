package domain.reminderschedule;

import static util.Helper.extractNumbers;

import domain.Schedule;
import domain.time.Time;
import domain.time.Timezone;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderSchedule implements Schedule {
    private static String ABSOLUTE_TIME_PATTERN = "(([0-9]{4}[\\-\\s\\/])?[0-9]{1,2}[\\-\\s\\/][0-9]{1,2}\\s)?[0-9]{1,2}[\\:h][0-9]{1,2}";
    private static String RELATIVE_TIME_PATTERN = "(in\\s)?(([0-9]+)\\s(days?|months?|years?|hours?|minutes?)(,|\\sand\\s)?\\s?)+(from\\snow)?";
    private Time time;
    private boolean absolute;

    public ReminderSchedule(Time time, boolean absolute) {
        this.time = time;
        this.absolute = absolute;
    }

    public static ReminderSchedule minutesFromNow(int minutes) {
        return new ReminderSchedule(Time.now().plusMinutes(minutes), false);
    }

    public static ReminderSchedule parse(String string) {
        string = string.toLowerCase();

        Time scheduledTime = null;
        boolean absolute = false;

        if (string.matches(ABSOLUTE_TIME_PATTERN)) {
            scheduledTime = parseAbsoluteTime(string);
            absolute = true;
        } else if (string.matches(RELATIVE_TIME_PATTERN)) {
            scheduledTime = parseRelativeTime(string);
            absolute = false;
        }

        if (scheduledTime != null && scheduledTime.isAfter(Time.now())) {
            return new ReminderSchedule(scheduledTime, absolute);
        }

        throw new InvalidReminderFormatException();
    }

    private static Time parseRelativeTime(String string) {
        Time scheduledTime;
        Time now = Time.now();
        Pattern p = Pattern.compile("([0-9]+)\\s(years?|months?|days?|hours?|minutes?)");
        Matcher m = p.matcher(string);
        scheduledTime = now;

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

        return scheduledTime;
    }

    private static Time parseAbsoluteTime(String string) {
        Time now = Time.now();
        Time scheduledTime;
        List<Integer> values = extractNumbers(string);
        Collections.reverse(values);

        int year = now.year();
        int month = now.month();
        int day = now.day();
        int hour = now.hour();
        int minute = now.minute();

        switch (values.size()) {
            case 5:
                year = values.get(4);
            case 4:
                month = values.get(3);
                day = values.get(2);
            case 2:
                hour = values.get(1);
                minute = values.get(0);
        }

        scheduledTime = Time.fromDate(year, month, day, hour, minute);
        return scheduledTime;
    }

    @Override
    public boolean isTriggered(Time time, Timezone timezone) {
        if (absolute) {
            time = time.withTimezone(timezone);
        }
        return this.time.equals(time);
    }
}

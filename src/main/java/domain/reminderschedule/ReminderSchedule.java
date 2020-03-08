package domain.reminderschedule;

import domain.Schedule;
import domain.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderSchedule implements Schedule {
    private Time time;

    public ReminderSchedule(Time time) {
        this.time = time;
    }

    @Override
    public boolean isTriggered(Time time) {
        return this.time.equals(time);
    }

    public static ReminderSchedule minutesFromNow(int minutes) {
        return new ReminderSchedule(Time.now().plusMinutes(minutes));
    }

    private static String EXACT_TIME_PATTERN = "(([0-9]{4}[\\-\\s\\/])?[0-9]{1,2}[\\-\\s\\/][0-9]{1,2}\\s)?[0-9]{1,2}[\\:h][0-9]{1,2}";
    private static String RELATIVE_TIME_PATTERN = "(in\\s)?(([0-9]+)\\s(days?|months?|years?|hours?|minutes?)(,|\\sand\\s)?\\s?)+(from\\snow)?";

    public static ReminderSchedule parse(String string) {
        Time now = Time.now();
        Time scheduledTime = null;
        string = string.toLowerCase();

        if (string.matches(EXACT_TIME_PATTERN)) {
            List<Integer> values = extractIntegers(string);

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
            
        } else if (string.matches(RELATIVE_TIME_PATTERN)) {
            Pattern p = Pattern.compile("([0-9]+)\\s(years?|months?|days?|hours?|minutes?)");
            Matcher m = p.matcher(string);
            scheduledTime = now;

            while (m.find()) {
                int value = Integer.parseInt(m.group(1));

                switch(m.group(2)) {
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
        }
        
        if (scheduledTime == null || scheduledTime.equals(now)) {
            throw new InvalidReminderFormatException();
        }

        return new ReminderSchedule(scheduledTime);
    }

    private static List<Integer> extractIntegers(String string) {
        List<Integer> list = new ArrayList<>();

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(string);
        while (m.find()) {
            int value = Integer.parseInt(m.group());
            list.add(value);
        }

        Collections.reverse(list);

        return list;
    }
}

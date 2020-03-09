package domain.schedule;

import domain.time.Time;
import domain.time.Timezone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeTimeSchedule implements Schedule {
    protected static String PATTERN = "^(in\\s)?(([0-9]+)\\s(days?|months?|years?|hours?|minutes?)(,|\\sand\\s)?\\s?)+(from\\snow)?$";

    private Time time;

    public RelativeTimeSchedule(Time time) {
        this.time = time;
    }

    public static RelativeTimeSchedule parse(String string, Time now) {
        string = string.toLowerCase();
        if (!string.matches(PATTERN)) {
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

        return new RelativeTimeSchedule(scheduledTime);
    }

    @Override
    public boolean isTriggered(Time time, Timezone timezone) {
        return this.time.equals(time);
    }
}

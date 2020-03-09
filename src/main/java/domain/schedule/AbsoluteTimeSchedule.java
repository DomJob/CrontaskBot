package domain.schedule;

import static util.Helper.extractNumbers;

import domain.time.Time;
import domain.time.Timezone;
import java.util.Collections;
import java.util.List;

public class AbsoluteTimeSchedule implements Schedule {
    protected static final String PATTERN = "^(([0-9]{4}[\\-\\s\\/])?[0-9]{1,2}[\\-\\s\\/][0-9]{1,2}\\s)?[0-9]{1,2}[\\:h][0-9]{1,2}$";

    private Time time;

    public AbsoluteTimeSchedule(Time time) {
        this.time = time;
    }

    public static AbsoluteTimeSchedule parse(String string, Time now) {
        string = string.toLowerCase();
        if (!string.matches(PATTERN)) {
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

        return new AbsoluteTimeSchedule(scheduledTime);
    }

    @Override
    public boolean isTriggered(Time time, Timezone timezone) {
        time = time.withTimezone(timezone);
        return this.time.equals(time);
    }
}

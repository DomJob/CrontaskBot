package domain;

public class Time {

    private int minutesSinceEpoch;

    public Time(int minutesSinceEpoch) {
        this.minutesSinceEpoch = minutesSinceEpoch;
    }

    public Time nextMinute() {
        return new Time(minutesSinceEpoch+1);
    }

    public static Time now() {
        // TODO - DateTime stuff
        return new Time(0);
    }

    public int year() {
        return 0;
    }

    public int month() {
        return 0;
    }

    public int day() {
        return 0;
    }

    public int hour() {
        return 0;
    }

    public int minute() {
        return 0;
    }
}

package domain.cronschedule;

import domain.Schedule;
import domain.Time;

public class CronSchedule implements Schedule {

    @Override
    public boolean isTriggered(Time time) {
        return false;
    }
}

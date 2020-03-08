package domain;

import domain.time.Time;
import domain.time.Timezone;

public interface Schedule {
    boolean isTriggered(Time time, Timezone timezone);
}

package domain.task;

import static domain.time.Time.max;
import static domain.time.Time.min;

import domain.schedule.Schedule;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;

public class Task {
    private final TaskId id;
    private final String name;
    private final User owner;
    private final Schedule schedule;
    private Time snoozedUntil = Time.NEVER;

    public Task(TaskId id, String name, User owner, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.schedule = schedule;
    }

    public boolean isTriggered(Time time) {
        Time inOwnerTimezone = time.withTimezone(owner.getTimezone());

        return time.equals(snoozedUntil) || schedule.isTriggered(inOwnerTimezone);
    }

    public void snoozeUntil(Time time) {
        snoozedUntil = time;
    }

    public TaskId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public Time scheduledFor(Time now) {
        Timezone timezone = owner.getTimezone();
        now = now.withTimezone(timezone);

        Time snoozedUntilInTimeZone = snoozedUntil.withTimezone(timezone);
        Time nextTrigger = schedule.nextTrigger(now);

        Time soonerTrigger = min(snoozedUntilInTimeZone, nextTrigger);
        Time laterTrigger = max(snoozedUntilInTimeZone, nextTrigger);

        if (now.isAfter(laterTrigger)) {
            return Time.NEVER;
        }


        return now.isAfter(soonerTrigger) ? laterTrigger : soonerTrigger;
    }
}

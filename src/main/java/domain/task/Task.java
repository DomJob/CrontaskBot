package domain.task;

import domain.schedule.Schedule;
import domain.time.Time;
import domain.user.User;

public class Task {
    private TaskId id;
    private String name;
    private User owner;
    private Schedule schedule;
    private Time snoozedUntil;

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
}

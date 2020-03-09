package domain.Task;

import domain.schedule.Schedule;
import domain.time.Time;
import domain.user.User;

public class Task {
    private long id;
    private String name;
    private User owner;
    private Schedule schedule;

    public Task(long id, String name, User owner, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.schedule = schedule;
    }

    public boolean isTriggered(Time time) {
        return schedule.isTriggered(time, owner.getTimezone());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }
}

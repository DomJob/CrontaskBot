package domain.task;

import domain.schedule.Schedule;
import domain.time.Time;
import domain.user.User;

public class Task {
    private TaskId id;
    private String name;
    private User owner;
    private Schedule schedule;

    public Task(TaskId id, String name, User owner, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.schedule = schedule;
    }

    public boolean isTriggered(Time time) {
        return schedule.isTriggered(time.withTimezone(owner.getTimezone()));
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

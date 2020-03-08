package domain;

public class Task {
    private long id;
    private String name;
    private long ownerId;
    private Schedule schedule;

    public Task(long id, String name, long ownerId, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.schedule = schedule;
    }

    public boolean isTriggered(Time time) {
        return schedule.isTriggered(time);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getOwnerId() {
        return ownerId;
    }
}

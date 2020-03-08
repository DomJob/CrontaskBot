package domain;

public class Task {
    private long id;
    private String name;
    private long ownerId;
    private Schedule schedule;
    private boolean isReminder;

    private Time lastChecked;

    public Task(long id, String name, long ownerId, Schedule schedule, boolean isReminder) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.schedule = schedule;
        this.isReminder = isReminder;

        this.lastChecked = Time.now();
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

    public boolean isReminder() {
        return isReminder;
    }
}

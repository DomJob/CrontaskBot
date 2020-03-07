package domain;

public class Task {
    private long id;
    private String name;
    private long ownerId;
    private Schedule schedule;

    private Time lastChecked;

    public Task(long id, String name, long ownerId, Schedule schedule) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.schedule = schedule;

        this.lastChecked = Time.now();
    }

    public boolean isTriggered(Time untilTime) {
        Time checkTime = lastChecked.nextMinute();
        lastChecked = untilTime;

        while(!checkTime.equals(untilTime)) {
            if(schedule.isTriggered(checkTime)) {
                return true;
            }
            checkTime = checkTime.nextMinute();
        }

        return false;
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

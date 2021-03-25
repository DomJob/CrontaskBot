package infrastructure.persistence.entities;

import domain.schedule.Schedule;
import domain.task.Task;
import domain.task.TaskId;
import domain.time.Time;

import java.lang.reflect.Field;

public class TaskDao {
    public long id;
    public String name;
    public UserDao owner;
    public String schedule;
    public long snoozedUntil;

    public TaskDao(long id, String name, UserDao owner, String schedule, long snoozedUntil) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.schedule = schedule;
        this.snoozedUntil = snoozedUntil;
    }

    public static TaskDao fromModel(Task task) {
        Class<? extends Task> taskClass = task.getClass();
        String scheduleStr = "0";
        long snoozedUntil = 0;

        try {
            Field scheduleField = taskClass.getDeclaredField("schedule");
            Field snoozedUntilField = taskClass.getDeclaredField("snoozedUntil");

            scheduleField.setAccessible(true);
            snoozedUntilField.setAccessible(true);

            scheduleStr = scheduleField.get(task).toString();

            Time val;
            if ((val = (Time) snoozedUntilField.get(task)) != null) {
                snoozedUntil = val.getMinutesSinceEpoch();
            }

            scheduleField.setAccessible(false);
            snoozedUntilField.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return new TaskDao(task.getId().toLong(),
                task.getName(),
                UserDao.fromModel(task.getOwner()),
                scheduleStr,
                snoozedUntil);
    }

    public Task toModel() {
        Task task = new Task(new TaskId(id), name, owner.toModel(), Schedule.deserialize(schedule));
        task.snoozeUntil(new Time(snoozedUntil));
        return task;
    }
}

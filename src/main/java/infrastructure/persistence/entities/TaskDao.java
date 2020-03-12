package infrastructure.persistence.entities;

import domain.schedule.Schedule;
import domain.task.Task;
import java.lang.reflect.Field;

public class TaskDao {
    public long id;
    public String name;
    public UserDao owner;
    public String schedule;

    public TaskDao(long id, String name, UserDao owner, String schedule) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.schedule = schedule;
    }

    public static TaskDao fromModel(Task task) {
        Class<? extends Task> taskClass = task.getClass();
        String scheduleStr = "0";

        try {
            Field field = taskClass.getDeclaredField("schedule");
            field.setAccessible(true);
            scheduleStr = field.get(task).toString();
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return new TaskDao(task.getId(),
            task.getName(),
            UserDao.fromModel(task.getOwner()),
            scheduleStr);
    }

    public Task toModel() {
        return new Task(id, name, owner.toModel(), Schedule.deserialize(schedule));
    }
}

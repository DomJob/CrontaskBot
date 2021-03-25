package domain.task;

import domain.schedule.Schedule;
import domain.user.User;
import domain.util.LongGenerator;

public class TaskFactory {
    private final LongGenerator longGenerator;

    public TaskFactory(LongGenerator longGenerator) {
        this.longGenerator = longGenerator;
    }

    public Task create(String name, User owner, Schedule schedule) {
        return new Task(new TaskId(longGenerator.generate()), name, owner, schedule);
    }
}

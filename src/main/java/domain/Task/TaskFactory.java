package domain.Task;

import domain.Schedule;
import domain.user.User;
import domain.util.LongGenerator;

public class TaskFactory {
    private LongGenerator longGenerator;

    public TaskFactory(LongGenerator longGenerator) {
        this.longGenerator = longGenerator;
    }

    public Task create(String name, User owner, Schedule schedule) {
        return new Task(longGenerator.generate(), name, owner, schedule);
    }
}

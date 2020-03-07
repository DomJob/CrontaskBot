package domain;

import domain.util.LongGenerator;

public class TaskFactory {
    private LongGenerator longGenerator;

    public TaskFactory(LongGenerator longGenerator) {
        this.longGenerator = longGenerator;
    }

    public Task create(String name, long ownerId, Schedule schedule) {
        return new Task(longGenerator.generate(), name, ownerId, schedule);
    }
}

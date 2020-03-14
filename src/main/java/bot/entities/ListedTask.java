package bot.entities;

import domain.task.Task;
import domain.time.Time;
import org.jetbrains.annotations.NotNull;

public class ListedTask implements Comparable<ListedTask> {
    public int index;
    public String name;
    public Time scheduledFor;
    public Task model;

    public ListedTask(int index, String name, Time scheduledFor, Task model) {
        this.index = index;
        this.name = name;
        this.scheduledFor = scheduledFor;
        this.model = model;
    }

    @Override
    public int compareTo(@NotNull ListedTask listedTask) {
        return scheduledFor.compareTo(listedTask.scheduledFor);
    }
}

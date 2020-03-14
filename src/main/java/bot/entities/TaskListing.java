package bot.entities;

import static java.lang.Integer.min;

import domain.task.Task;
import domain.time.Time;
import java.util.ArrayList;
import java.util.List;

public class TaskListing {
    private static final int TASKS_PER_PAGE = 10;

    private List<ListedTask> listedTasks;
    private int nbTasks;
    private int start;

    public TaskListing(List<Task> tasks, Time now) {
        listedTasks = new ArrayList<>();

        for (Task task : tasks) {
            Time scheduledFor = task.scheduledFor(now);
            if (scheduledFor.equals(Time.NEVER) || scheduledFor.equals(now)) {
                continue;
            }

            listedTasks.add(new ListedTask(-1, task.getName(), scheduledFor, task));
        }

        listedTasks.sort(ListedTask::compareTo);
        int i = 1;
        for (ListedTask ltask : listedTasks) {
            ltask.index = i++;
        }

        nbTasks = listedTasks.size();
        start = 0;
    }

    public List<ListedTask> getPage() {
        return listedTasks.subList(getStart(), getEnd());
    }

    public void nextPage() {
        if (!hasNextPage()) {
            throw new IllegalStateException();
        }

        start += TASKS_PER_PAGE;
    }

    public void previousPage() {
        if (!hasPreviousPage()) {
            throw new IllegalStateException();
        }

        start -= TASKS_PER_PAGE;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return min(nbTasks, start + TASKS_PER_PAGE);
    }

    public int size() {
        return nbTasks;
    }

    public boolean hasNextPage() {
        return start + TASKS_PER_PAGE < nbTasks;
    }

    public boolean hasPreviousPage() {
        return start > 0;
    }

    public boolean empty() {
        return listedTasks.isEmpty();
    }

    public Task getTask(int index) {
        return listedTasks.get(index).model;
    }
}

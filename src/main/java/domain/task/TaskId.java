package domain.task;

import java.util.Objects;

public class TaskId {
    private long value;

    public TaskId(long value) {
        this.value = value;
    }

    public static TaskId fromString(String s) {
        return new TaskId(Long.parseLong(s));
    }

    public long toLong() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskId taskId = (TaskId) o;
        return value == taskId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}

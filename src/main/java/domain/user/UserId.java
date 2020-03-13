package domain.user;

import java.util.Objects;

public class UserId {
    private long value;

    public UserId(long value) {
        this.value = value;
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
        UserId taskId = (UserId) o;
        return value == taskId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserId{" +
            "value=" + value +
            '}';
    }
}

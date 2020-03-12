package domain.time;

import static infrastructure.util.Helper.extractNumbers;

import java.util.List;
import java.util.Objects;

public class Timezone {
    public static final Timezone UTC = new Timezone(0);
    private static final String TIMEZONE_OFFSET_PATTERN = "^[-+]?([0-9]+)(:([0-9]+))?$";
    private int offset;

    private Timezone(int offset) {
        this.offset = offset;
    }

    public static Timezone fromString(String string) {
        if (!string.matches(TIMEZONE_OFFSET_PATTERN)) {
            throw new IllegalArgumentException();
        }

        List<Integer> numbers = extractNumbers(string);

        int hours = numbers.get(0);
        int minutes = numbers.size() == 2 ? numbers.get(1) : 0;

        if (hours > 14 || minutes >= 60) {
            throw new IllegalArgumentException();
        }

        int offset = hours * 60 + minutes;

        if (string.startsWith("-")) {
            offset *= -1;
        }

        return new Timezone(offset);
    }

    public static Timezone fromOffset(int offset) {
        return new Timezone(offset);
    }

    public int getOffset() {
        return offset;
    }

    public String toString() {
        int hours = offset / 60;
        int minutes = Math.abs(offset % 60);

        return String.format("%s%02d:%02d", offset >= 0 ? "+" : "", hours, minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Timezone timezone = (Timezone) o;
        return offset == timezone.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset);
    }
}

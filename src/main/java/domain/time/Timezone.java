package domain.time;

import static util.Helper.extractNumbers;

import java.util.List;
import java.util.Objects;

public class Timezone {
    public static final Timezone UTC = new Timezone(0);

    private int offset;

    public Timezone(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    private static final String TIMEZONE_OFFSET_PATTERN = "^\\-?([0-9]+)(\\:([0-9]+))?$";

    public static Timezone parse(String string) {
        if(!string.matches(TIMEZONE_OFFSET_PATTERN)) {
            throw new InvalidTimezoneOffsetException();
        }

        List<Integer> numbers = extractNumbers(string);

        int hours = numbers.get(0);
        int minutes = numbers.size() == 2 ? numbers.get(1) : 0;

        if(hours > 14 || minutes >= 60) {
            throw new InvalidTimezoneOffsetException();
        }

        int offset = hours * 60 + minutes;

        if(string.startsWith("-")) {
            offset *= -1;
        }

        return new Timezone(offset);
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
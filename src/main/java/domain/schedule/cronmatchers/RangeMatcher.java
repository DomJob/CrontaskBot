package domain.schedule.cronmatchers;

import domain.schedule.InvalidScheduleException;

public class RangeMatcher extends CronMatcher {
    private final int start;
    private final int end;

    public RangeMatcher(int start, int end) {
        if (start >= end) {
            throw new InvalidScheduleException();
        }

        this.start = start;
        this.end = end;
    }

    @Override
    public boolean match(int value) {
        return value >= start && value <= end;
    }
}

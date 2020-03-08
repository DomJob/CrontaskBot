package domain.cronschedule.matchers;

import domain.cronschedule.CronMatcher;
import domain.cronschedule.InvalidCronFormatException;

public class RangeMatcher extends CronMatcher {
    private int start;
    private int end;

    public RangeMatcher(int start, int end) {
        if(start >= end) {
            throw new InvalidCronFormatException();
        }

        this.start = start;
        this.end = end;
    }

    @Override
    public boolean match(int value) {
        return value >= start && value <= end;
    }
}

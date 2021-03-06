package domain.schedule.cronmatchers;

import domain.schedule.InvalidScheduleException;

import java.util.HashSet;
import java.util.Set;

public abstract class CronMatcher {
    private static final String ANY_MATCHER_FORMAT = "^\\*$";
    private static final String SPECIFIC_MATCHER_FORMAT = "^[0-9]+$";
    private static final String LIST_MATCHER_FORMAT = "^([0-9]+,)+[0-9]+$";
    private static final String RANGE_MATCHER_FORMAT = "^[0-9]+-[0-9]+$";
    private static final String STEP_MATCHER_FORMAT = "^\\*/[0-9]+$";

    public static CronMatcher parse(String string) {
        if (string.matches(ANY_MATCHER_FORMAT)) {
            return new AnyMatcher();
        } else if (string.matches(SPECIFIC_MATCHER_FORMAT)) {
            int value = Integer.parseInt(string);

            return new SpecificMatcher(value);
        } else if (string.matches(LIST_MATCHER_FORMAT)) {
            Set<Integer> values = new HashSet<>();

            String[] stringValues = string.split(",");

            for (String stringValue : stringValues) {
                values.add(Integer.parseInt(stringValue));
            }

            return new ListMatcher(values);
        } else if (string.matches(RANGE_MATCHER_FORMAT)) {
            String[] stringValues = string.split("-");

            int start = Integer.parseInt(stringValues[0]);
            int end = Integer.parseInt(stringValues[1]);

            return new RangeMatcher(start, end);
        } else if (string.matches(STEP_MATCHER_FORMAT)) {
            String[] stringValues = string.split("/");

            int step = Integer.parseInt(stringValues[1]);

            return new StepMatcher(step);
        } else {
            throw new InvalidScheduleException();
        }
    }

    public abstract boolean match(int value);
}

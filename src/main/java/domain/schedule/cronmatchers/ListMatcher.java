package domain.schedule.cronmatchers;

import java.util.Set;

public class ListMatcher extends CronMatcher {
    private Set<Integer> values;

    public ListMatcher(Set<Integer> values) {
        this.values = values;
    }

    @Override
    public boolean match(int value) {
        return values.contains(value);
    }
}

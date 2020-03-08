package domain.cronschedule.matchers;

import domain.cronschedule.CronMatcher;

public class AnyMatcher extends CronMatcher {
    @Override
    public boolean match(int value) {
        return true;
    }
}

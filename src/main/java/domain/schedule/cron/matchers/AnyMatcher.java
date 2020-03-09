package domain.schedule.cron.matchers;

import domain.schedule.cron.CronMatcher;

public class AnyMatcher extends CronMatcher {
    @Override
    public boolean match(int value) {
        return true;
    }
}

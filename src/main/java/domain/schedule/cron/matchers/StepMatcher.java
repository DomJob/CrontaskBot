package domain.schedule.cron.matchers;

import domain.schedule.cron.CronMatcher;

public class StepMatcher extends CronMatcher {
    private int step;

    public StepMatcher(int step) {
        this.step = step;
    }

    @Override
    public boolean match(int value) {
        return value % step == 0;
    }
}

package domain.schedule.cronmatchers;

public class AnyMatcher extends CronMatcher {
    @Override
    public boolean match(int value) {
        return true;
    }
}

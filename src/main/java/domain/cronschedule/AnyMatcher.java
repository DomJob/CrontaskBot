package domain.cronschedule;

public class AnyMatcher extends CronMatcher {
    @Override
    public boolean match(int value) {
        return true;
    }
}

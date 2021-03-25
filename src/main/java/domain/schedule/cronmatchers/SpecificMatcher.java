package domain.schedule.cronmatchers;

public class SpecificMatcher extends CronMatcher {
    private final int value;

    public SpecificMatcher(int value) {
        this.value = value;
    }

    @Override
    public boolean match(int value) {
        return this.value == value;
    }
}

package domain.cronschedule;

public class SpecificMatcher extends CronMatcher {
    private int value;

    public SpecificMatcher(int value) {
        this.value = value;
    }

    @Override
    public boolean match(int value) {
        return this.value == value;
    }
}

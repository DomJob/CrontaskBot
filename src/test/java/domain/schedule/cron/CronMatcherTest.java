package domain.schedule.cron;

import static org.junit.Assert.*;

import org.junit.Test;

public class CronMatcherTest {
    @Test
    public void anyMatcher_matchesAnyValue() {
        CronMatcher matcher = CronMatcher.parse("*");

        assertTrue(matcher.match(25));
        assertTrue(matcher.match(38));
        assertTrue(matcher.match(14));
    }

    @Test
    public void listMatcher_matchesElementInList() {
        CronMatcher matcher = CronMatcher.parse("12,46,56");

        assertTrue(matcher.match(12));
        assertTrue(matcher.match(46));
        assertTrue(matcher.match(56));
    }

    @Test
    public void listMatcher_doesntMatchesElementInList() {
        CronMatcher matcher = CronMatcher.parse("12,46,56");

        assertFalse(matcher.match(18));
        assertFalse(matcher.match(25));
        assertFalse(matcher.match(96));
    }

    @Test
    public void rangeMatcher_matchElementsInRange() {
        CronMatcher matcher = CronMatcher.parse("12-18");

        assertTrue(matcher.match(12));
        assertTrue(matcher.match(15));
        assertTrue(matcher.match(18));
    }

    @Test
    public void rangeMatcher_doesntMatchElementsInRange() {
        CronMatcher matcher = CronMatcher.parse("12-18");

        assertFalse(matcher.match(11));
        assertFalse(matcher.match(7));
        assertFalse(matcher.match(19));
        assertFalse(matcher.match(25));
    }

    @Test
    public void stepMatcher_matchElementsInStep() {
        CronMatcher matcher = CronMatcher.parse("*/5");

        assertTrue(matcher.match(0));
        assertTrue(matcher.match(5));
        assertTrue(matcher.match(10));
        assertTrue(matcher.match(55));
    }

    @Test
    public void stepMatcher_doesntMatchElementsNotInStep() {
        CronMatcher matcher = CronMatcher.parse("*/5");

        assertFalse(matcher.match(3));
        assertFalse(matcher.match(46));
        assertFalse(matcher.match(16));
    }

    @Test
    public void specificMatcher_matchSpecificValue() {
        CronMatcher matcher = CronMatcher.parse("15");

        assertTrue(matcher.match(15));
    }

    @Test
    public void specificMatcher_doesntMatchOtherValues() {
        CronMatcher matcher = CronMatcher.parse("15");

        assertFalse(matcher.match(19));
        assertFalse(matcher.match(4));
        assertFalse(matcher.match(9));
    }

    @Test(expected = InvalidCronFormatException.class)
    public void invalidFormat_throwsException_1() {
        CronMatcher.parse("asdas");
    }

    @Test(expected = InvalidCronFormatException.class)
    public void invalidFormat_throwsException_2() {
        CronMatcher.parse("*-");
    }

    @Test(expected = InvalidCronFormatException.class)
    public void invalidFormat_throwsException_3() {
        CronMatcher.parse("/8");
    }
}
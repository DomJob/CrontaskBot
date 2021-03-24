package domain.user;

import domain.time.Timezone;

public class User {
    private UserId id;
    private Timezone timezone;

    public User(UserId id, Timezone timezone) {
        this.id = id;
        this.timezone = timezone;
    }

    public User(UserId id) {
        this.id = id;
        this.timezone = Timezone.UTC;
    }

    public Language getLanguage() {
        return Language.FRENCH;
    }

    public UserId getId() {
        return id;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }
}

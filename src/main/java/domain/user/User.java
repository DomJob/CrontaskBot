package domain.user;

import domain.time.Timezone;

public class User {
    private UserId id;
    private Timezone timezone;
    private Language language;

    public User(UserId id, Timezone timezone) {
        this.id = id;
        this.timezone = timezone;
        this.language = Language.ENGLISH;
    }

    public User(UserId id, Timezone timezone, Language language) {
        this.id = id;
        this.timezone = timezone;
        this.language = language;
    }

    public User(UserId id) {
        this.id = id;
        this.timezone = Timezone.UTC;
    }

    public Language getLanguage() {
        return language;
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

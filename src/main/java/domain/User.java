package domain;

import domain.time.Timezone;

public class User {
    private long id;
    private Timezone timezone;

    public User(long id, Timezone timezone) {
        this.id = id;
        this.timezone = timezone;
    }

    public User(long id) {
        this.id = id;
        this.timezone = Timezone.UTC;
    }

    public long getId() {
        return id;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }
}

package infrastructure.persistence.entities;

import domain.time.Timezone;
import domain.user.User;

public class UserDao {
    public long id;
    public int tzOffset;

    public UserDao(long id, int tzOffset) {
        this.id = id;
        this.tzOffset = tzOffset;
    }

    public static UserDao fromModel(User user) {
        return new UserDao(user.getId(), user.getTimezone().getOffset());
    }

    public User toModel() {
        return new User(id, Timezone.fromOffset(tzOffset));
    }
}

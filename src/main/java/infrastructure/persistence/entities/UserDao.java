package infrastructure.persistence.entities;

import domain.time.Timezone;
import domain.user.Language;
import domain.user.User;
import domain.user.UserId;

public class UserDao {
    public long id;
    public int tzOffset;
    public String language;

    public UserDao(long id, int tzOffset, String language) {
        this.id = id;
        this.tzOffset = tzOffset;
        this.language = language;
    }

    public static UserDao fromModel(User user) {
        return new UserDao(user.getId().toLong(),
            user.getTimezone().getOffset(),
            user.getLanguage().getCode());
    }

    public User toModel() {
        return new User(new UserId(id), Timezone.fromOffset(tzOffset), Language.lookup(language));
    }
}

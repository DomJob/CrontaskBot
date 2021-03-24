package infrastructure.persistence;

import domain.user.User;
import domain.user.UserId;
import domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryInMemory implements UserRepository {
    private final Map<UserId, User> users = new HashMap<>();

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}

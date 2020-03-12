package infrastructure.persistence;

import domain.user.User;
import domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryInMemory implements UserRepository {
    private Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}

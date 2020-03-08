package infrastructure.persistence.inmemory;

import domain.user.User;
import domain.user.UserRepository;
import java.util.HashMap;
import java.util.Map;

public class UserRepositoryInMemory implements UserRepository {
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User findById(long id) {
        if (!users.containsKey(id)) {
            save(new User(id));
        }

        return users.get(id);
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}

package service;

import domain.time.Timezone;
import domain.user.User;
import domain.user.UserRepository;
import java.util.Optional;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            User newUser = new User(id);
            userRepository.save(newUser);
            return newUser;
        }
    }

    public void changeTimezone(User user, Timezone timezone) {
        user.setTimezone(timezone);
        userRepository.save(user);
    }
}

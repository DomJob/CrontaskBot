package service;

import domain.time.Timezone;
import domain.user.Language;
import domain.user.User;
import domain.user.UserId;
import domain.user.UserRepository;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(UserId id) {
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

    public void changeLanguage(User user, Language language) {
        user.setLanguage(language);
        userRepository.save(user);
    }
}

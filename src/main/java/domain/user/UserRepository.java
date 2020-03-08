package domain.user;

public interface UserRepository {
    User findById(long id);

    void save(User user);
}

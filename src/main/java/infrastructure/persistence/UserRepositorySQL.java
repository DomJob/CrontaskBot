package infrastructure.persistence;

import static infrastructure.persistence.Sqlite.getConnection;

import domain.user.User;
import domain.user.UserRepository;
import infrastructure.persistence.entities.UserDao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositorySQL implements UserRepository {
    private static final String FIND_USER_BY_ID = "SELECT tzOffset FROM user WHERE id = ?";
    private static final String INSERT_USER = "INSERT INTO user VALUES(?, ?)";
    private static final String UPDATE_USER = "UPDATE user SET tzOffset = ? WHERE id = ?";

    @Override
    public Optional<User> findById(long id) {
        User user = null;

        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_USER_BY_ID);

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int tzOffset = rs.getInt("tzOffset");

                UserDao dao = new UserDao(id, tzOffset);

                user = dao.toModel();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        if (findById(user.getId()).isPresent()) {
            updateUser(user);
        } else {
            insertUser(user);
        }
    }

    private void updateUser(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(UPDATE_USER);
            UserDao dao = UserDao.fromModel(user);

            statement.setInt(1, dao.tzOffset);
            statement.setLong(2, dao.id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUser(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_USER);
            UserDao dao = UserDao.fromModel(user);

            statement.setLong(1, dao.id);
            statement.setInt(2, dao.tzOffset);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package infrastructure.persistence;

import static infrastructure.persistence.Sqlite.getConnection;

import domain.time.Timezone;
import domain.user.User;
import domain.user.UserRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositorySQL implements UserRepository {
    private static final String FIND_USER_BY_ID = "SELECT tzOffset FROM user WHERE id = ?";
    private static final String INSERT_USER = "INSERT INTO user VALUES(?, ?)";
    private static final String UPDATE_USER = "UPDATE user SET tzOffset = ? WHERE id = ?";

    @Override
    public User findById(long id) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(FIND_USER_BY_ID);

            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                int tzOffset = rs.getInt("tzOffset");

                return new User(id, Timezone.fromOffset(tzOffset));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // TODO Optional
    }

    @Override
    public void save(User user) {
        if(findById(user.getId()) == null) {
            insertUser(user);
            return;
        }

        try {
            PreparedStatement statement = getConnection().prepareStatement(UPDATE_USER);

            statement.setInt(1, user.getTimezone().getOffset());
            statement.setLong(2, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertUser(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_USER);

            statement.setLong(1, user.getId());
            statement.setInt(2, user.getTimezone().getOffset());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
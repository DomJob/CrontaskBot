package infrastructure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlite {
    private static Connection connection;
    private static String path = "crontaskbot.db";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + path);
                enforceSchema();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return connection;
    }

    private static void enforceSchema() {
        try {
            Statement statement = getConnection().createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user(" +
                "id INTEGER PRIMARY KEY," +
                "tzOffset INTEGER)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS task(" +
                "id INTEGER PRIMARY KEY," +
                "name VARCHAR," +
                "owner INTEGER," +
                "schedule VARCHAR," +
                "snoozedUntil INTEGER," +
                "FOREIGN KEY(owner) REFERENCES user(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPath(String path) {
        Sqlite.path = path;
    }
}

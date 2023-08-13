import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL = DbConfig.getDbUrl();
    private static final String USERNAME = DbConfig.getDbUsername();
    private static final String PASSWORD = DbConfig.getDbPassword();

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }
    }
}
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = DbConfig.class.getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
}
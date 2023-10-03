package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    // Private constructor to initialize the data source
    private CustomDataSource(String driver, String url, String name, String password) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;

        // You may perform additional initialization here if needed
    }

    // Create a new instance of CustomDataSource (thread-safe)
    public static CustomDataSource getInstance(String driver, String url, String name, String password) {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    instance = new CustomDataSource(driver, url, name, password);
                }
            }
        }
        return instance;
    }

    // Implementation of DataSource interface methods

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(name, password);
    }

    @Override
    public Connection getConnection(String username, String userPassword) throws SQLException {
        try {
            // Load the JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Failed to load JDBC driver: " + driver, e);
        }

        // Set up properties for the connection (if needed)
        Properties properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", userPassword);

        // Establish a connection to the database
        return DriverManager.getConnection(url, properties);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    // Other DataSource interface methods (not shown here)

    // You can also add methods for custom behavior or configuration as needed
}

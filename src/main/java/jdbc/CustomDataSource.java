package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
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

    private CustomDataSource(String driver, String url, String name, String password) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
    }

    // Create a new instance of CustomDataSource (thread-safe)
    public static CustomDataSource getInstance() {
        if (instance == null) {
            Properties properties = new Properties();
            try {
                properties.load(CustomConnector.class.getClassLoader().getResourceAsStream("app.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String driver = properties.getProperty("postgres.driver");
            String url = properties.getProperty("postgres.url");
            String name = properties.getProperty("postgres.name");
            String password = properties.getProperty("postgres.password");

            return new CustomDataSource(driver,url, name, password);
        }
        return instance;

    }

    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
            return new CustomConnector().getConnection(url, username, password);
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
}

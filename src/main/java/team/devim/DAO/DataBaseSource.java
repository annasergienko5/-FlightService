package team.devim.DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import team.devim.Constants;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(Constants.DB_URL);
        config.setUsername(Constants.DB_USERNAME);
        config.setPassword(Constants.DB_PASSWORD);
        config.setDriverClassName(Constants.DB_DRIVER);
        config.setMaximumPoolSize(5);
        ds = new HikariDataSource(config);
    }

    private DataBaseSource() {}

    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }
}
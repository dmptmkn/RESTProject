package org.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;

@UtilityClass
public class ConnectionManager {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("root");
        config.setPassword("root");
        dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public static Connection getConnection() {
        return dataSource.getConnection();
    }
}

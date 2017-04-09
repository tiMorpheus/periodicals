package com.tolochko.periodicals.model.dao.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolProvider {
    private static final Logger logger = Logger.getLogger(ConnectionPoolProvider.class);
    private static ConnectionPoolProvider connectionProvider;
    private static DataSource dataSource;

    public ConnectionPoolProvider(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public ConnectionPoolProvider(String url, String username, String password) {
        if (dataSource == null){
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        }
    }

    public ConnectionPoolProvider(Properties properties){
        if (dataSource == null){
            HikariConfig config = new HikariConfig(properties);
            dataSource = new HikariDataSource(config);
        }
    }


    public static synchronized  Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Cannot create connection from data source", e);
        }

        return null;
    }
}

package com.tolochko.periodicals.model.dao.pool;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.connection.AbstractConnectionImpl;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolProvider {
    private static final Logger logger = Logger.getLogger(ConnectionPoolProvider.class);

    private static ConnectionPool INSTANCE;

    static {
        InputStream input;
        Properties properties = new Properties();
        try {

            input = ConnectionPoolProvider.class.getClassLoader()
                    .getResourceAsStream("config/database.properties");
            properties.load(input);
            INSTANCE = createPoolFromProperties(properties);

        } catch (FileNotFoundException e) {
            logger.error("Exception during opening the db-config", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Exception during loading db-config properties", e);
            throw new RuntimeException();
        }
    }

    private static ConnectionPool createPoolFromProperties(Properties properties) {
        String url = properties.getProperty("database.url");
        String dbName = properties.getProperty("database.name");
        String userName = properties.getProperty("database.username");
        String userPassword = properties.getProperty("database.password");
        int maxConnNumber = Integer.parseInt(properties.getProperty("database.maxconnections"));


        logger.debug("getting pool impl url: " + url + " db name: " + dbName);
        return ConnectionPoolImpl.getBuilder(url)
                .setUserName(userName)
                .setPassword(userPassword)
                .setMaxConnections(maxConnNumber)
                .build();
    }

    public static ConnectionPool getPool() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Connection manager hasn't initialized yet!");
        }
        return INSTANCE;
    }
}

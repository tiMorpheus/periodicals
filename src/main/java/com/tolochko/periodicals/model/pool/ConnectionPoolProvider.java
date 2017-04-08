package com.tolochko.periodicals.model.pool;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolProvider {
    private static final Logger logger = Logger.getLogger(ConnectionPoolProvider.class);


    private static ConnectionPoolProvider connectionProvider;
    private static DataSource dataSource;

    private ConnectionPoolProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Cannot create connection from data source", e);
        }

        return null;
    }

    public static ConnectionPoolProvider getInstance(){
        if (connectionProvider == null){
            logger.info("Connection manager must be initWithJNDI");
            throw new IllegalArgumentException();
        }

        return connectionProvider;
    }

    public static synchronized void initWithJNDI(String name){
        if (connectionProvider != null){
            return;
        }

        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:/comp/env");
            DataSource ds = (DataSource) env.lookup(name);

            connectionProvider = new ConnectionPoolProvider(ds);

        } catch (NamingException e) {
            logger.error("Cannot create initial context");
        }
    }

    public synchronized static void initWithDataSource(DataSource ds){
        if (connectionProvider != null){
            return;
        }

        connectionProvider = new ConnectionPoolProvider(ds);
    }



}

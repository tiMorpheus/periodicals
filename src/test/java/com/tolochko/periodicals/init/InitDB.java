package com.tolochko.periodicals.init;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.tolochko.periodicals.model.pool.ConnectionPoolProvider;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitDB {
    private static boolean isDataBaseIsReady = false;

    public static void initMySql() {
        if (isDataBaseIsReady) {
            return;
        }
        DataSource ds = getMySqlDS();
        ConnectionPoolProvider.initWithDataSource(ds);

        isDataBaseIsReady = true;
    }

    private static DataSource getMySqlDS() {
        Properties properties = new Properties();
        MysqlDataSource ds = new MysqlDataSource();
        try {
            InputStream fis = ClassLoader.getSystemResourceAsStream("database.properties");
            properties.load(fis);
            ds.setURL(properties.getProperty("mysql.database.url"));
            ds.setUser(properties.getProperty("mysql.database.username"));
            ds.setPassword(properties.getProperty("mysql.database.password"));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return ds;
    }
}

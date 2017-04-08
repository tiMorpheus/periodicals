package com.tolochko.periodicals.init;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.tolochko.periodicals.model.pool.ConnectionPoolProvider;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            FileInputStream fis = new FileInputStream("./src/main/resources/dbConfig.properties");
            properties.load(fis);
            ds.setURL(properties.getProperty("database.urlTest"));
            ds.setUser(properties.getProperty("database.userName"));
            ds.setPassword(properties.getProperty("database.userPassword"));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return ds;
    }
}

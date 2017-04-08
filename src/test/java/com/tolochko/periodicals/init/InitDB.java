package com.tolochko.periodicals.init;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitDB {
    public static DataSource getMySqlDS() {
        Properties properties = new Properties();
        MysqlDataSource ds = new MysqlDataSource();

        InputStream fis = ClassLoader.getSystemResourceAsStream("database.properties");
        try {
            properties.load(fis);
        } catch (IOException e) {

        }
        ds.setUrl(properties.getProperty("mysql.database.url"));
        ds.setUser(properties.getProperty("mysql.database.username"));
        ds.setPassword(properties.getProperty("mysql.database.password"));

        return ds;
    }
}

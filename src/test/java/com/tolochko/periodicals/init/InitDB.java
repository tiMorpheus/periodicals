package com.tolochko.periodicals.init;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.tolochko.periodicals.model.pool.ConnectionPoolProvider;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class InitDB {


    public static void initMySql(){
        ConnectionPoolProvider provider;
        Properties properties = new Properties();

        try {
            InputStream fis = ClassLoader.getSystemResourceAsStream("database.properties");
            properties.load(fis);
            properties.setProperty("dataSourceClassName", "mysql.databases.classname");
            properties.setProperty("dataSource.user", "mysql.database.username");
            properties.setProperty("dataSource.password", "mysql.database.password");
            properties.setProperty("dataSource.databaseName", "mysql.database.name");
            properties.put("dataSource.logWriter", new PrintWriter(System.out));

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

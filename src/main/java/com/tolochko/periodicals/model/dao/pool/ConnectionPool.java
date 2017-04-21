package com.tolochko.periodicals.model.dao.pool;

import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();
}

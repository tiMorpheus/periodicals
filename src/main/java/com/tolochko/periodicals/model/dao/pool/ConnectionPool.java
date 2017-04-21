package com.tolochko.periodicals.model.dao.pool;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;

public interface ConnectionPool {

    AbstractConnection getConnection();
}

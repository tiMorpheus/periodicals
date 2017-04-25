package com.tolochko.periodicals.model;

import com.tolochko.periodicals.model.connection.ConnectionProxy;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;

import static jdk.internal.dynalink.support.Guards.isNull;

public class TransactionHelper {
    private final static ThreadLocal<ConnectionProxy> connections = new ThreadLocal<>();

    public static ConnectionProxy getConnectionProxy() {
        ConnectionProxy conn = connections.get();

        if (conn == null) {
            conn = ConnectionPoolProvider.getPool().getConnection();
        }

        return conn;
    }

    public static void beginTransaction() {
        try {

            ConnectionProxy connection = ConnectionPoolProvider.getPool().getConnection();
            connections.set(connection);
            connection.beginTransaction();

        } catch (RuntimeException e){

        }
    }

    public static void rollback() {
        if (connections.get() == null){
            throw new DaoException("can't rollback without beginTransaction");
        }
        connections.get().rollbackTransaction();
    }

    public static void commit() {
        if (connections.get() == null){
            throw new DaoException("can't commit without beginTransaction");
        }
        connections.get().commitTransaction();
    }

}

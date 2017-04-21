package com.tolochko.periodicals.model.transaction;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface Transaction {
    Logger logger = Logger.getLogger(Transaction.class);

    void pass();

    static void doTransaction(Transaction tx, int transactionIsolationLevel) {
        ConnectionPool pool = ConnectionPoolProvider.getPool();
        Connection connection = pool.getConnection();
        try {
            boolean isAutoCommited = connection.getAutoCommit();
            connection.setAutoCommit(false);
            int oldIsolation = connection.getTransactionIsolation();
            if (oldIsolation != transactionIsolationLevel) {
                connection.setTransactionIsolation(transactionIsolationLevel);
            }
            tx.pass();
            if (isAutoCommited) {
                connection.commit();
                connection.setAutoCommit(isAutoCommited);
            }
            if (connection.getTransactionIsolation() != oldIsolation) {
                connection.setTransactionIsolation(oldIsolation);
            }

        } catch (SQLException | DaoException e) {
            logger.error("transaction failed", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("can't rollback", e1);
            }

            throw new DaoException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("can't close", e);
            }
        }
    }

    static void doTransaction(Transaction tx) {
        doTransaction(tx, Connection.TRANSACTION_READ_COMMITTED);
    }
}

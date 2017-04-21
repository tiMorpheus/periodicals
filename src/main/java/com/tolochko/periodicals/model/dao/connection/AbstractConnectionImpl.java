package com.tolochko.periodicals.model.dao.connection;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractConnectionImpl implements AbstractConnection {
    private static final Logger logger = Logger.getLogger(AbstractConnectionImpl.class);
    private Connection connection;
    private boolean transactionBegun = false;
    private boolean transactionCommitted = false;

    private static final String CAN_NOT_BEGIN_TRANSACTION = "Can not begin transaction.";
    private static final String CAN_NOT_COMMIT_TRANSACTION = "Can not commit transaction";
    private static final String CAN_NOT_ROLLBACK_TRANSACTION = "Can not rollback transaction";
    private static final String CAN_NOT_CLOSE_CONNECTION = "Can not close connection";

    public AbstractConnectionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void beginTransaction() {
        try {
            connection.setAutoCommit(false);
            transactionBegun = true;
        } catch (SQLException e) {
            logger.error(CAN_NOT_BEGIN_TRANSACTION, e);
            throw new DaoException(CAN_NOT_BEGIN_TRANSACTION, e);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            transactionCommitted = true;
        } catch (SQLException e) {
            logger.error(CAN_NOT_COMMIT_TRANSACTION, e);
            throw new DaoException(CAN_NOT_COMMIT_TRANSACTION, e);
        }

    }

    @Override
    public void rollbackTransaction() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            transactionCommitted = true;
        } catch (SQLException e) {
            logger.error(CAN_NOT_ROLLBACK_TRANSACTION, e);
            throw new DaoException(CAN_NOT_ROLLBACK_TRANSACTION, e);
        }
    }

    @Override
    public void close() {
        try {
            if (transactionBegun && !transactionCommitted) {
                rollbackTransaction();
            }
            connection.close();
        } catch (SQLException e) {
            logger.error(CAN_NOT_CLOSE_CONNECTION, e);
            throw new DaoException(CAN_NOT_CLOSE_CONNECTION, e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

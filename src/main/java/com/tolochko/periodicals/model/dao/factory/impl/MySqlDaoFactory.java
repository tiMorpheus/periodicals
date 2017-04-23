package com.tolochko.periodicals.model.dao.factory.impl;

import com.tolochko.periodicals.model.connection.ConnectionProxy;
import com.tolochko.periodicals.model.connection.ConnectionProxyImpl;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.impl.*;
import com.tolochko.periodicals.model.dao.interfaces.*;
import org.apache.log4j.Logger;

import java.sql.Connection;

import static java.util.Objects.isNull;

public class MySqlDaoFactory implements DaoFactory {
    private static final Logger logger = Logger.getLogger(MySqlDaoFactory.class);
    private static final DaoFactory DAO_FACTORY_INSTANCE = new MySqlDaoFactory();

    private MySqlDaoFactory() {
    }

    public static DaoFactory getFactoryInstance() {
        return DAO_FACTORY_INSTANCE;
    }

    @Override
    public UserDao getUserDao(ConnectionProxy connection) {
        checkConnection(connection);
        return new UserDaoImpl(getConnectionProxy(connection));
    }

    @Override
    public PeriodicalDao getPeriodicalDao(ConnectionProxy connection) {
        checkConnection(connection);
        return new PeriodicalDaoImpl(getConnectionProxy(connection));
    }

    @Override
    public SubscriptionDao getSubscriptionDao(ConnectionProxy connection) {
        checkConnection(connection);
        return new SubscriptionDaoImpl(getConnectionProxy(connection));
    }

    @Override
    public InvoiceDao getInvoiceDao(ConnectionProxy connection) {
        checkConnection(connection);
        return new InvoiceDaoImpl(getConnectionProxy(connection));
    }

    @Override
    public RoleDao getRoleDao(ConnectionProxy connection) {
        checkConnection(connection);
        return new RoleDaoImpl(getConnectionProxy(connection));
    }

    private void checkConnection(ConnectionProxy connection) {
        if (isNull(connection)) {
            logger.error("Connection can not be null.");
            throw new DaoException("Connection can not be null.");
        }

        if (!(connection instanceof ConnectionProxyImpl)) {
            logger.error("Connection is not an ConnectionProxyImpl for JDBC.");
            throw new DaoException("Connection is not an ConnectionProxyImpl for JDBC.");
        }
    }

    private Connection getConnectionProxy(ConnectionProxy conn) {
        return ((ConnectionProxyImpl) conn).getConnection();
    }
}

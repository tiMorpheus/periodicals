package com.tolochko.periodicals.model.dao.factory.impl;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.connection.AbstractConnectionImpl;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.impl.*;
import com.tolochko.periodicals.model.dao.interfaces.*;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.apache.log4j.Logger;

import java.sql.Connection;

import static java.util.Objects.isNull;

public class MySqlDaoFactory implements DaoFactory {
    private static final Logger logger = Logger.getLogger(MySqlDaoFactory.class);

    private static final DaoFactory DAO_FACTORY_INSTANCE = new MySqlDaoFactory();

    // TODO: 13.04.2017 dao realization
/*
    private static UserDao userDao ;
    private static PeriodicalDao periodicalDao ;
    private static SubscriptionDao subscriptionDao ;
    private static InvoiceDao invoiceDao ;
*/

    private MySqlDaoFactory() {
    }

    public static DaoFactory getFactoryInstance() {
        return DAO_FACTORY_INSTANCE;
    }

    @Override
    public PeriodicalDao getPeriodicalDao(AbstractConnection connection) {
        checkConnection(connection);
        return new PeriodicalDaoImpl(getConnection(connection));
    }

    @Override
    public UserDao getUserDao(AbstractConnection connection) {
        checkConnection(connection);
        return new UserDaoImpl(getConnection(connection));
    }

    @Override
    public SubscriptionDao getSubscriptionDao(AbstractConnection connection) {
        checkConnection(connection);
        return new SubscriptionDaoImpl(getConnection(connection));
    }

    @Override
    public InvoiceDao getInvoiceDao(AbstractConnection connection) {
        checkConnection(connection);
        return new InvoiceDaoImpl(getConnection(connection));
    }

    @Override
    public RoleDao getRoleDao(AbstractConnection connection) {
        checkConnection(connection);
        return new RoleDaoImpl(getConnection(connection));
    }

    private void checkConnection(AbstractConnection conn) {
        if (isNull(conn)) {
            throw new DaoException("Connection can not be null.");
        }
    }

    private Connection getConnection(AbstractConnection conn){
        return ((AbstractConnectionImpl) conn).getConnection();
    }
}

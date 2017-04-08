package com.tolochko.periodicals.model.dao.factories.impl;

import com.tolochko.periodicals.model.dao.factories.DaoFactory;
import com.tolochko.periodicals.model.dao.impl.mysql.UserDaoImpl;
import com.tolochko.periodicals.model.dao.interfaces.*;
import org.apache.log4j.Logger;

public class MySqlDaoFactory extends DaoFactory {
    private static final Logger logger = Logger.getLogger(MySqlDaoFactory.class);

    private static final DaoFactory DAO_FACTORY_INSTANCE = new MySqlDaoFactory();

    private static UserDao userDao = new UserDaoImpl();

    // TODO: 07.04.2017 initialize instances dao
    private static InvoiceDao invoiceDao;
    private static RoleDao roleDao;
    private static SubscriptionDao subscriptionDao;
    private static PeriodicalDao periodicalDao;


    private MySqlDaoFactory() {
    }


    public static DaoFactory getFactoryInstance(){
        return DAO_FACTORY_INSTANCE;
    }

    @Override
    public PeriodicalDao getPeriodicalDao() {
        return periodicalDao;
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public RoleDao getRoleDao() {
        return roleDao;
    }

    @Override
    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    @Override
    public InvoiceDao getInvoiceDao() {
        return invoiceDao;
    }
}

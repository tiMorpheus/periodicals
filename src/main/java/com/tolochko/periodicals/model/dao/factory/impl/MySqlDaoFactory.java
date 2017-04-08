package com.tolochko.periodicals.model.dao.factory.impl;

import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.impl.mysql.InvoiceDaoImpl;
import com.tolochko.periodicals.model.dao.impl.mysql.PeriodicalDaoImpl;
import com.tolochko.periodicals.model.dao.impl.mysql.SubscriptionDaoImpl;
import com.tolochko.periodicals.model.dao.impl.mysql.UserDaoImpl;
import com.tolochko.periodicals.model.dao.interfaces.InvoiceDao;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import org.apache.log4j.Logger;

public class MySqlDaoFactory implements DaoFactory {
    private static final Logger logger = Logger.getLogger(MySqlDaoFactory.class);

    private static final DaoFactory DAO_FACTORY_INSTANCE = new MySqlDaoFactory();

    private static UserDao userDao = new UserDaoImpl();

    private static PeriodicalDao periodicalDao = new PeriodicalDaoImpl();
    private static SubscriptionDao subscriptionDao = new SubscriptionDaoImpl();
    private static InvoiceDao invoiceDao = new InvoiceDaoImpl();


    private MySqlDaoFactory() {
    }


    public static DaoFactory getFactoryInstance() {
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
    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    @Override
    public InvoiceDao getInvoiceDao() {
        return invoiceDao;
    }
}

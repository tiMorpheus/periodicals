package com.tolochko.periodicals.model.dao.factories;

import com.tolochko.periodicals.model.dao.factories.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.InvoiceDao;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;

public abstract class DaoFactory {

    public abstract PeriodicalDao getPeriodicalDao();

    public abstract UserDao getUserDao();

    public abstract SubscriptionDao getSubscriptionDao();

    public abstract InvoiceDao getInvoiceDao();

    public static DaoFactory getMysqlDaoFactory() {
        return MySqlDaoFactory.getFactoryInstance();
    }
}

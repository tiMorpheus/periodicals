package com.tolochko.periodicals.model.dao.factories;

import com.tolochko.periodicals.model.dao.factories.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.*;

public abstract class DaoFactory {

    public abstract PeriodicalDao getPeriodicalDao();

    public abstract UserDao getUserDao();

    public abstract RoleDao getRoleDao();

    public abstract SubscriptionDao getSubscriptionDao();

    public abstract InvoiceDao getInvoiceDao();

    public static DaoFactory getMysqlDaoFactory() {
        return MySqlDaoFactory.getFactoryInstance();
    }
}

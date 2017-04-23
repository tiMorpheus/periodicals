package com.tolochko.periodicals.model.dao.factory;

import com.tolochko.periodicals.model.connection.ConnectionProxy;
import com.tolochko.periodicals.model.dao.interfaces.*;

public interface DaoFactory {

    PeriodicalDao getPeriodicalDao(ConnectionProxy connection);

    UserDao getUserDao(ConnectionProxy connection);

    SubscriptionDao getSubscriptionDao(ConnectionProxy connection);

    InvoiceDao getInvoiceDao(ConnectionProxy connection);

    RoleDao getRoleDao(ConnectionProxy connection);
}

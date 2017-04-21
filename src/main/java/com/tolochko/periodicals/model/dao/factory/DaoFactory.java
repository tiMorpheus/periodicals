package com.tolochko.periodicals.model.dao.factory;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.interfaces.*;

public interface DaoFactory {

    PeriodicalDao getPeriodicalDao(AbstractConnection connection);

    UserDao getUserDao(AbstractConnection connection);

    SubscriptionDao getSubscriptionDao(AbstractConnection connection);

    InvoiceDao getInvoiceDao(AbstractConnection connection);

    RoleDao getRoleDao(AbstractConnection connection);
}

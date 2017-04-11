package com.tolochko.periodicals.model.dao.factory;

import com.tolochko.periodicals.model.dao.interfaces.InvoiceDao;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;

public interface DaoFactory {

    PeriodicalDao getPeriodicalDao();

    UserDao getUserDao();

    SubscriptionDao getSubscriptionDao();

    InvoiceDao getInvoiceDao();
}

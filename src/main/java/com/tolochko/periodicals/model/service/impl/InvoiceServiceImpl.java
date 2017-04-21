package com.tolochko.periodicals.model.service.impl;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.InvoiceDao;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import com.tolochko.periodicals.model.domain.FinancialStatistics;
import com.tolochko.periodicals.model.domain.invoice.Invoice;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.InvoiceService;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.Objects.isNull;


public class InvoiceServiceImpl implements InvoiceService {
    private static final Logger logger = Logger.getLogger(InvoiceServiceImpl.class);
    private static final InvoiceServiceImpl instance = new InvoiceServiceImpl();
    private DaoFactory factory = MySqlDaoFactory.getFactoryInstance();
    private ConnectionPool pool = ConnectionPoolProvider.getPool();

    private InvoiceServiceImpl(){}

    public static InvoiceServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Invoice findOneById(long invoiceId) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getInvoiceDao(conn).findOneById(invoiceId);
        }
    }

    @Override
    public List<Invoice> findAllByUserId(long userId) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getInvoiceDao(conn).findAllByUserId(userId);
        }
    }

    @Override
    public List<Invoice> findAllByPeriodicalId(long periodicalId) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getInvoiceDao(conn).findAllByPeriodicalId(periodicalId);
        }
    }

    @Override
    public void createNew(Invoice invoice) {
        try (AbstractConnection conn = pool.getConnection()) {
            factory.getInvoiceDao(conn).add(invoice);
        }
    }

    @Override
    public boolean payInvoice(Invoice invoiceToPay) {

        AbstractConnection conn = null;
        try  {
            conn = pool.getConnection();
            conn.beginTransaction();
            SubscriptionDao subscriptionDao = factory.getSubscriptionDao(conn);
            invoiceToPay.setStatus(Invoice.Status.PAID);
            invoiceToPay.setPaymentDate(Instant.now());

            User userFromDb = factory.getUserDao(conn).findOneById(invoiceToPay.getUser().getId());
            Periodical periodical = invoiceToPay.getPeriodical();

            Subscription existingSubscription = subscriptionDao
                    .findOneByUserIdAndPeriodicalId(userFromDb.getId(), periodical.getId());

            factory.getInvoiceDao(conn).updateById(invoiceToPay.getId(), invoiceToPay);

            int subscriptionPeriod = invoiceToPay.getSubscriptionPeriod();

            if (isNull(existingSubscription)) {
                createAndPersistNewSubscription(userFromDb, periodical, subscriptionPeriod, subscriptionDao);
            } else {
                updateExistingSubscription(existingSubscription, subscriptionPeriod, subscriptionDao);
            }

            conn.commitTransaction();
            return true;
        } catch (RuntimeException e){
            conn.rollbackTransaction();
            logger.error("transaction failed");
            throw new DaoException(e);
        } finally {
            conn.close();
        }
    }

    @Override
    public FinancialStatistics getFinStatistics(Instant since, Instant until) {
        try (AbstractConnection conn = pool.getConnection()) {
            InvoiceDao dao = factory.getInvoiceDao(conn);
            long totalInvoiceSum = dao.getCreatedInvoiceSumByCreationDate(since, until);
            long paidInvoiceSum = dao.getPaidInvoiceSumByPaymentDate(since, until);

            return new FinancialStatistics(totalInvoiceSum, paidInvoiceSum);
        }
    }

    private void updateExistingSubscription(Subscription existingSubscription,
                                            int subscriptionPeriod, SubscriptionDao subscriptionDao) {
        Instant newEndDate;

        if (Subscription.Status.INACTIVE.equals(existingSubscription.getStatus())) {
            newEndDate = getEndDate(Instant.now(), subscriptionPeriod);
        } else {
            newEndDate = getEndDate(existingSubscription.getEndDate(), subscriptionPeriod);
        }

        existingSubscription.setEndDate(newEndDate);
        existingSubscription.setStatus(Subscription.Status.ACTIVE);

        subscriptionDao.updateById(existingSubscription.getId(), existingSubscription);
    }

    private void createAndPersistNewSubscription(User userFromDb, Periodical periodical,
                                                 int subscriptionPeriod, SubscriptionDao subscriptionDao) {
        Subscription.Builder builder = new Subscription.Builder();

        builder.setUser(userFromDb)
                .setPeriodical(periodical)
                .setDeliveryAddress(userFromDb.getAddress())
                .setEndDate(getEndDate(Instant.now(), subscriptionPeriod))
                .setStatus(Subscription.Status.ACTIVE);

        subscriptionDao.add(builder.build());
    }

    private Instant getEndDate(Instant startInstant, int subscriptionPeriod) {
        LocalDateTime startDate = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());

        return startDate.plusMonths(subscriptionPeriod).toInstant(ZoneOffset.UTC);
    }
}

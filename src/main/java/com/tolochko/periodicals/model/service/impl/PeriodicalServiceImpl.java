package com.tolochko.periodicals.model.service.impl;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import com.tolochko.periodicals.model.domain.PeriodicalNumberByCategory;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import com.tolochko.periodicals.model.service.PeriodicalService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// TODO: 13.04.2017 REALIZATION 



public class PeriodicalServiceImpl implements PeriodicalService {
    private static final Logger logger = Logger.getLogger(PeriodicalServiceImpl.class);
    private static final PeriodicalServiceImpl instance = new PeriodicalServiceImpl();
    private DaoFactory factory = MySqlDaoFactory.getFactoryInstance();
    private ConnectionPool pool = ConnectionPoolProvider.getPool();


    private PeriodicalServiceImpl(){}

    public static PeriodicalServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Periodical findOneById(long id) {
        try (AbstractConnection conn = pool.getConnection()) {
            PeriodicalDao periodicalDao = factory.getPeriodicalDao(conn);
            return periodicalDao.findOneById(id);
        }
    }

    @Override
    public Periodical findOneByName(String name) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getPeriodicalDao(conn).findOneByName(name);
        }
    }

    @Override
    public List<Periodical> findAll() {
        try (AbstractConnection connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).findAll();
        }
    }

    @Override
    public List<Periodical> findAllByStatus(Periodical.Status status) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getPeriodicalDao(conn).findAllByStatus(status);
        }
    }

    @Override
    public Periodical save(Periodical periodical) {
        if (periodical.getId() == 0) {
           createNewPeriodical(periodical);
        } else {
            updatePeriodical(periodical);
        }

        return getPeriodicalFromDbByName(periodical.getName());
    }


    private void createNewPeriodical(Periodical periodical) {
        try (AbstractConnection conn = pool.getConnection()) {
            factory.getPeriodicalDao(conn).add(periodical);
        }
    }

    private void updatePeriodical(Periodical periodical) {
        try (AbstractConnection conn = pool.getConnection()) {
            int affectedRows = factory.getPeriodicalDao(conn).updateById(periodical.getId(),periodical);

            if (affectedRows == 0) {
                throw new NoSuchElementException(
                        String.format("There is no periodical in the DB with id = %d", periodical.getId()));
            }
        }
    }

    private Periodical getPeriodicalFromDbByName(String name) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getPeriodicalDao(conn).findOneByName(name);
        }
    }

    @Override
    public int updateAndSetDiscarded(Periodical periodical) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getPeriodicalDao(conn).updateAndSetDiscarded(periodical);
        }
    }

    @Override
    public int deleteAllDiscarded() {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getPeriodicalDao(conn).deleteAllDiscarded();
        }
    }

    @Override
    public boolean hasActiveSubscriptions(long periodicalId) {
        try (AbstractConnection conn = pool.getConnection()) {

            return !factory.getSubscriptionDao(conn)
                    .findAllByPeriodicalIdAndStatus(periodicalId, Subscription.Status.ACTIVE)
                    .isEmpty();
        }
    }

    @Override
    public List<PeriodicalNumberByCategory> getQuantitativeStatistics() {
        List<PeriodicalNumberByCategory> statistics = new ArrayList<>();

        try (AbstractConnection conn = pool.getConnection()) {
            PeriodicalDao dao = factory.getPeriodicalDao(conn);

            for (PeriodicalCategory category : PeriodicalCategory.values()) {
                statistics.add(getPeriodicalNumberByCategory(dao, category));
            }

            return statistics;
        }
    }

    private PeriodicalNumberByCategory getPeriodicalNumberByCategory(PeriodicalDao dao,
                                                                     PeriodicalCategory category) {
        int active = dao.findNumberOfPeriodicalsWithCategoryAndStatus(category,
                Periodical.Status.ACTIVE);
        int inActive = dao.findNumberOfPeriodicalsWithCategoryAndStatus(category,
                Periodical.Status.INACTIVE);
        int discarded = dao.findNumberOfPeriodicalsWithCategoryAndStatus(category,
                Periodical.Status.DISCARDED);

        return PeriodicalNumberByCategory.newBuilder(category)
                .setActive(active)
                .setInActive(inActive)
                .setDiscarded(discarded)
                .build();
    }

}

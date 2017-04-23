package com.tolochko.periodicals.model.service.impl;

import com.tolochko.periodicals.model.connection.ConnectionProxy;
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


public class PeriodicalServiceImpl implements PeriodicalService {
    private static final Logger logger = Logger.getLogger(PeriodicalServiceImpl.class);
    private static final PeriodicalServiceImpl instance = new PeriodicalServiceImpl();
    private DaoFactory factory = MySqlDaoFactory.getFactoryInstance();
    private ConnectionPool pool = ConnectionPoolProvider.getPool();

    private PeriodicalServiceImpl() {
    }

    public static PeriodicalServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Periodical findOneById(long id) {

        try (ConnectionProxy connection = pool.getConnection()) {


            return factory.getPeriodicalDao(connection).findOneById(id);
        }

    }

    @Override
    public Periodical findOneByName(String name) {
        try (ConnectionProxy connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).findOneByName(name);
        }
    }

    @Override
    public List<Periodical> findAll() {
        try (ConnectionProxy connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).findAll();
        }
    }

    @Override
    public List<Periodical> findAllByStatus(Periodical.Status status) {
        try (ConnectionProxy connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).findAllByStatus(status);
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
        try (ConnectionProxy connection = pool.getConnection()) {
            factory.getPeriodicalDao(connection).add(periodical);
        }
    }

    private void updatePeriodical(Periodical periodical) {
        try (ConnectionProxy connection = pool.getConnection()) {
            int affectedRows = factory.getPeriodicalDao(connection).updateById(periodical.getId(), periodical);

            if (affectedRows == 0) {
                throw new NoSuchElementException(
                        String.format("There is no periodical in the DB with id = %d", periodical.getId()));
            }
        }
    }

    private Periodical getPeriodicalFromDbByName(String name) {
        try (ConnectionProxy connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).findOneByName(name);
        }
    }

    @Override
    public int updateAndSetDiscarded(Periodical periodical) {
        try (ConnectionProxy connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).updateAndSetDiscarded(periodical);
        }
    }

    @Override
    public int deleteAllDiscarded() {
        try (ConnectionProxy connection = pool.getConnection()) {
            return factory.getPeriodicalDao(connection).deleteAllDiscarded();
        }
    }

    @Override
    public boolean hasActiveSubscriptions(long periodicalId) {
        try (ConnectionProxy connection = pool.getConnection()) {
            return !factory.getSubscriptionDao(connection)
                    .findAllByPeriodicalIdAndStatus(periodicalId, Subscription.Status.ACTIVE)
                    .isEmpty();
        }
    }

    @Override
    public List<PeriodicalNumberByCategory> getQuantitativeStatistics() {
        List<PeriodicalNumberByCategory> statistics = new ArrayList<>();

        try (ConnectionProxy connection = pool.getConnection()) {
            PeriodicalDao dao = factory.getPeriodicalDao(connection);

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

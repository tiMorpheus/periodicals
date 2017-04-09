package com.tolochko.periodicals.dao.periodical;

import com.tolochko.periodicals.init.EntityCreator;
import com.tolochko.periodicals.init.InitDB;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PeriodicalDaoImplTest {
    private static ConnectionPoolProvider pool;
    private static DaoFactory factory;
    private static PeriodicalDao periodicalDao;
    private static Periodical expected;
    private static long id;

    @BeforeClass
    public static void setUp() {
        pool = new ConnectionPoolProvider(InitDB.getMySqlDS());
        factory = MySqlDaoFactory.getFactoryInstance();
        periodicalDao = factory.getPeriodicalDao();

        expected = EntityCreator.createPeriodical();
    }

    @Test
    public void add_Test() {
        id = periodicalDao.add(expected);
        assertPeriodicalData(periodicalDao.findOneById(id));
    }

    private void assertPeriodicalData(Periodical actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected.getPublisher(), actual.getPublisher());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getOneMonthCost(), actual.getOneMonthCost());
        assertEquals(expected.getStatus(), actual.getStatus());
    }


    @Test
    public void findPeriodicalByNameAndPublisher() throws Exception {
        id = periodicalDao.add(expected);

        String name = expected.getName();
        String publisher = expected.getPublisher();

        assertPeriodicalData(periodicalDao.findPeriodicalByNameAndPublisher(name, publisher));

    }

    @Test
    public void findAllByStatus() throws Exception {

        Periodical.Status active = Periodical.Status.ACTIVE;
        Periodical.Status discarded = Periodical.Status.DISCARDED;
        Periodical.Status inactive = Periodical.Status.INACTIVE;

        id = periodicalDao.add(expected);

        List<Periodical> activePeriodicals = periodicalDao.findAllByStatus(active);
        List<Periodical> discardedPeriodicals = periodicalDao.findAllByStatus(discarded);
        List<Periodical> inactivePeriodicals = periodicalDao.findAllByStatus(inactive);

        assertEquals(2, activePeriodicals.size());
        assertEquals(0, discardedPeriodicals.size());
        assertEquals(1, inactivePeriodicals.size());
    }

    @Test
    public void findAllByCategory() throws Exception {

    }

    @Test
    public void updateAndSetDiscarded() throws Exception {

    }

    @Test
    public void deleteAllDiscarded() throws Exception {

        //add discarded periodical
        id = periodicalDao.add(expected);

        periodicalDao.deleteAllDiscarded();
        List<Periodical> periodicals = periodicalDao.findAllByStatus(Periodical.Status.DISCARDED);
        assertEquals(0, periodicals.size());

    }


    @Test
    public void findAll() throws Exception {

        List<Periodical> periodicals = periodicalDao.findAll();

        //expected - current number of rows in periodicals
        assertEquals(2, periodicals.size());

    }

    @Test
    public void updateById() throws Exception {
        id = periodicalDao.add(expected);

        assertEquals(PeriodicalCategory.NEWS, periodicalDao.findOneById(id).getCategory());
        expected.setCategory(PeriodicalCategory.ART);
        periodicalDao.updateById(id, expected);

        assertEquals(PeriodicalCategory.ART, periodicalDao.findOneById(id).getCategory());
    }


    @After
    public void tear_down() {
        periodicalDao.delete(id);
    }


}

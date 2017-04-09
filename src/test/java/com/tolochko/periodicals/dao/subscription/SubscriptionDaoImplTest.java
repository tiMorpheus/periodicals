package com.tolochko.periodicals.dao.subscription;

import com.tolochko.periodicals.init.EntityCreator;
import com.tolochko.periodicals.init.InitDB;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SubscriptionDaoImplTest {
    private static ConnectionPoolProvider pool;
    private static DaoFactory factory;
    private static SubscriptionDao subscriptionDao;
    private static Subscription expected;
    private static Long id;


    @BeforeClass
    public static void setUp() {
        pool = new ConnectionPoolProvider(InitDB.getMySqlDS());
        factory = MySqlDaoFactory.getFactoryInstance();
        subscriptionDao = factory.getSubscriptionDao();

        expected = EntityCreator.createSubscription();
    }


    @Test
    public void add() throws Exception {
        id = subscriptionDao.add(expected);

        assertSubscriptionData(subscriptionDao.findOneById(id));
    }

    private void assertSubscriptionData(Subscription actual) {
        assertEquals(actual.getUserId(), expected.getUserId());
        assertEquals(actual.getPeriodicalId(), expected.getPeriodicalId());
        assertEquals(actual.getDeliveryAddress(), expected.getDeliveryAddress());
        assertEquals(actual.getEndDate(), expected.getEndDate());
        assertEquals(actual.getStatus(), expected.getStatus());
    }

    @Test
    public void findByUserIdAndPeriodicalId() throws Exception {
        id = subscriptionDao.add(expected);

        long user_id = expected.getUserId();
        long periodical_id = expected.getPeriodicalId();

        assertSubscriptionData(subscriptionDao.findByUserIdAndPeriodicalId(user_id, periodical_id));

    }

    @Test
    public void findAll() throws Exception {
        id = subscriptionDao.add(expected);
        expected.setDeliveryAddress("testAddress");
        Long id2 = subscriptionDao.add(expected);

        List<Subscription> subscriptions = subscriptionDao.findAll();

        // must be number of rows in subscription
        assertEquals(2, subscriptions.size());

        subscriptionDao.delete(id2);
    }

    @Test
    public void updateById() throws Exception {
        id = subscriptionDao.add(expected);
        expected.setDeliveryAddress("new Address");

        subscriptionDao.updateById(id, expected);

        assertEquals("new Address", subscriptionDao.findOneById(id).getDeliveryAddress());
    }

    @After
    public void tearDown() {

        subscriptionDao.delete(id);
    }

}
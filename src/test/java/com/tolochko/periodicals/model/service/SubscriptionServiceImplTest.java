package com.tolochko.periodicals.model.service;

import com.tolochko.periodicals.model.connection.ConnectionProxy;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.impl.SubscriptionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubscriptionServiceImplTest {

    private static final long USER_ID = 3;
    @Mock
    private DaoFactory factory;
    @Mock
    private UserDao userDao;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private ConnectionProxy conn;
    @InjectMocks
    private SubscriptionService subscriptionService = SubscriptionServiceImpl.getInstance();
    private User user;
    private List<Subscription> userSubscriptions = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        user = new User();
        userSubscriptions.add(new Subscription());

        when(connectionPool.getConnection()).thenReturn(conn);

        when(factory.getUserDao(conn)).thenReturn(userDao);
        when(factory.getSubscriptionDao(conn)).thenReturn(subscriptionDao);

        when(userDao.findOneById(USER_ID)).thenReturn(user);
        when(subscriptionDao.findAllByUser(user)).thenReturn(userSubscriptions);
    }

    @Test
    public void findAllByUserId_Should_ReturnAllSubscriptionOfTheUser() throws Exception {
        assertEquals(userSubscriptions, subscriptionService.findAllByUserId(USER_ID));

        verify(subscriptionDao).findAllByUser(user);
    }
}

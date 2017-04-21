package com.tolochko.periodicals.model.service;

import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.RoleDao;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private static final String TEST_USERNAME = "admin";
    @Mock
    private DaoFactory factory;
    @Mock
    private UserDao userDao;
    @Mock
    private RoleDao roleDao;
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private Connection conn;
    @InjectMocks
    private UserService userService = UserServiceImpl.getInstance();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(connectionPool.getConnection()).thenReturn(conn);

        when(factory.getUserDao()).thenReturn(userDao);
        when(factory.getRoleDao()).thenReturn(roleDao);

    }

    @Test
    public void findOneUserByUserName_Should_SetRolesAndReturnCorrectUser() throws Exception {
        User user = mock(User.class);
        when(userDao.findOneByUserName(TEST_USERNAME)).thenReturn(user);

        assertEquals(user, userService.findOneUserByUserName(TEST_USERNAME));

        verify(user).setRole(any());
    }
}

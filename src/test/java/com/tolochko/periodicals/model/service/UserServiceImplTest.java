package com.tolochko.periodicals.model.service;

import com.tolochko.periodicals.model.connection.ConnectionProxy;
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
import java.util.ArrayList;
import java.util.List;

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
    private ConnectionProxy conn;
    @InjectMocks
    private UserService userService = UserServiceImpl.getInstance();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(connectionPool.getConnection()).thenReturn(conn);

        when(factory.getUserDao(conn)).thenReturn(userDao);
        when(factory.getRoleDao(conn)).thenReturn(roleDao);
    }

    @Test
    public void findOneUserByUserName_Should_SetRolesAndReturnCorrectUser() throws Exception {
        User user = mock(User.class);
        when(userDao.findOneByUserName(TEST_USERNAME)).thenReturn(user);

        assertEquals(user, userService.findOneUserByUserName(TEST_USERNAME));

        verify(user).setRole(any());
    }

    @Test
    public void findOneUserById_Should_SetRolesAndReturnCorrectUser() throws Exception{
        User user = mock(User.class);
        long id = 1;

        when(userDao.findOneById(id)).thenReturn(user);

        assertEquals(user, userService.findOneById(id));

        verify(user).setRole(any());
    }

    @Test
    public void findAllUsers_Should_SetRolesAndReturnCorrectUser() throws Exception {
        List<User> users = new ArrayList<>();

        User user = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        users.add(user);
        users.add(user2);
        users.add(user3);

        when(userDao.findAll()).thenReturn(users);

        assertEquals(users, userService.findAll());

        verify(user, atLeastOnce()).setRole(any());
        verify(user2, atLeastOnce()).setRole(any());
        verify(user3, atLeastOnce()).setRole(any());
    }

}

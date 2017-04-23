package com.tolochko.periodicals.model.dao;

import com.tolochko.periodicals.model.InitDb;
import com.tolochko.periodicals.model.connection.ConnectionProxy;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import com.tolochko.periodicals.model.domain.user.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static java.util.Objects.nonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserDaoImplTest {
    private static final long ADMIN_ID = 1;
    private static UserDao userDao;
    private static ConnectionProxy conn;
    private static DaoFactory factory;
    private static User expected;

    @BeforeClass
    public static void setUp() throws Exception {
        conn = InitDb.getTestPool().getConnection();
        factory = MySqlDaoFactory.getFactoryInstance();
        userDao = factory.getUserDao(conn);

        User.Builder userBuilder = new User.Builder();
        userBuilder.setUsername("admin")
                .setFirstName("Tymur")
                .setLastName("Tolochko")
                .setEmail("admin@gmail.com")
                .setStatus(User.Status.ACTIVE);

        expected = userBuilder.build();
    }

    @Test
    public void findOneById_Should_ReturnCorrectUser() throws Exception {
        assertUserData(userDao.findOneById(ADMIN_ID));
    }

    private void assertUserData(User actual) {
        assertEquals("UserName", expected.getUsername(), actual.getUsername());
        assertEquals("FirstName", expected.getFirstName(), actual.getFirstName());
        assertEquals("LastName", expected.getLastName(), actual.getLastName());
        assertEquals("Email", expected.getEmail(), actual.getEmail());
        assertEquals("Status", expected.getStatus(), actual.getStatus());
    }

    @Test
    public void findUserByUserName_Should_ReturnCorrectUser() {
        assertUserData(userDao.findOneByUserName("admin"));
    }

    @Test
    public void findUserByUserName_Should_ReturnNull() {
        assertNull(userDao.findOneByUserName("admin2"));
    }

    @Test
    public void findAll_Should_ReturnAllUsersFromDb() {
        int expectedNumber = 5;
        int actualNumber = userDao.findAll().size();

        assertEquals(expectedNumber, actualNumber);
    }


    @AfterClass
    public static void tearDown() throws SQLException {
        if (nonNull(conn)) {
            conn.close();
        }
    }
}

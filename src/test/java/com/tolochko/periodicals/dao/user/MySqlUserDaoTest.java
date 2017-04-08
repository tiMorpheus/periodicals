package com.tolochko.periodicals.dao.user;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.tolochko.periodicals.init.EntityCreator;
import com.tolochko.periodicals.init.InitDB;
import com.tolochko.periodicals.model.dao.factories.DaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.domain.user.User;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class MySqlUserDaoTest {
    private static UserDao userDao;
    private static DaoFactory factory;
    private static User expected;
    private static User recrut;
    private static MysqlDataSource ds;


    @BeforeClass
    public static void setUp() {
        InitDB.initMySql();
        factory = DaoFactory.getMysqlDaoFactory();
        userDao = factory.getUserDao();

        expected = EntityCreator.createUser();
    }

    @Test
    public void insertUserTest() {

        long id = userDao.createNew(expected);
        assertUserData(userDao.findOneById(id));
    }


    private void assertUserData(User actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getStatus(), actual.getStatus());
    }


    @Test
    public void findUserByEmailTest() {
        userDao.createNew(expected);
        assertUserData(userDao.findUserByEmail("tymurtolochko@gmail.com"));
    }

    @Test
    public void isEmailExistsInDbTest() {


        assertFalse(userDao.isEmailExistsInDb("tymurtolochko@gmail.com"));

        userDao.createNew(expected);

        assertTrue(userDao.isEmailExistsInDb("tymurtolochko@gmail.com"));

    }

    @Test
    public void findAllTest() {
        recrut = EntityCreator.createUser();
        recrut.setEmail("Test1");

        userDao.createNew(expected);
        userDao.createNew(recrut);

        List<User> users = userDao.findAll();

        //expected of current number of rows in table users
        assertEquals(2, users.size());

        userDao.deleteUserFromDb("Test1");
    }


    @After
    public void tearDown() throws SQLException {
        userDao.deleteUserFromDb(expected.getEmail());
    }
}

package com.tolochko.periodicals.dao.user;

import com.tolochko.periodicals.init.EntityCreator;
import com.tolochko.periodicals.init.InitDB;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserDaoImplTest {
    static ConnectionPoolProvider pool;
    private static UserDao userDao;
    private static DaoFactory factory;
    private static User expected;
    private static User recrut;
    Long id ;


    @BeforeClass
    public static void setUp() {
        pool = new ConnectionPoolProvider(InitDB.getMySqlDS());
        factory = MySqlDaoFactory.getFactoryInstance();
        userDao = factory.getUserDao();

        expected = EntityCreator.createUser();
    }

    @Test
    public void insertUserTest() {

        id = userDao.add(expected);
        assertUserData(userDao.findOneById(id));
    }


    private void assertUserData(User actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getRole(), actual.getRole());
    }


    @Test
    public void findUserByEmailTest() {
        id = userDao.add(expected);
        assertUserData(userDao.findUserByEmail("tymurtolochko@gmail.com"));
    }


    @Test
    public void findAllTest() {
        recrut = EntityCreator.createUser();
        recrut.setEmail("Test1");

        id = userDao.add(expected);
        Long recrutID = userDao.add(recrut);

        List<User> users = userDao.findAll();

        //expected of current number of rows in table users
        assertEquals(3, users.size());

        userDao.delete(recrutID);
    }

    @Test
    public void readUserRole_Test(){
        id = userDao.add(expected);

        assertEquals(User.Role.USER , userDao.readRole(id));
    }

    @Test
    public void updateUser_Test(){
        id = userDao.add(expected);

        recrut = EntityCreator.createUser();
        recrut.setEmail("test updateById");

        userDao.updateById(id, recrut);

        User updatedUser = userDao.findOneById(id);

        assertEquals("test updateById", updatedUser.getEmail());
    }

    @After
    public void tearDown() throws SQLException {
        userDao.delete(id);
    }
}

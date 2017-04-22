package com.tolochko.periodicals.model.dao;

import com.tolochko.periodicals.init.EntityCreator;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.domain.user.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class UserDaoImplTest {

    static DaoFactory factory = MySqlDaoFactory.getFactoryInstance();
    static UserDao userDao ;


    @Before
    public void setUp(){
        userDao = factory.getUserDao();
    }

    @Ignore
    public void addUser_should_add_user_into_db(){
        User userToAdd = EntityCreator.createUser();

        userDao.add(userToAdd);


    }

}

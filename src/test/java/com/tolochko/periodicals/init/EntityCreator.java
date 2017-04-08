package com.tolochko.periodicals.init;

import com.tolochko.periodicals.dao.user.MySqlUserDaoTest;
import com.tolochko.periodicals.model.dao.factories.DaoFactory;
import com.tolochko.periodicals.model.dao.factories.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.UserDao;
import com.tolochko.periodicals.model.domain.user.User;

public class EntityCreator {

    private static DaoFactory daoFactory = MySqlDaoFactory.getFactoryInstance();
    private static UserDao userDao = daoFactory.getUserDao();

    public static User createUser(){
        User.Builder userBuilder = new User.Builder();
        userBuilder.setFirstName("Tymur")
                .setLastName("Tolochko")
                .setEmail("tymurtolochko@gmail.com")
                .setAddress("Mazepy Street , 14")
                .setStatus(User.Status.ACTIVE)
                .setPassword("passwordd");

        return userBuilder.build();
    }

}

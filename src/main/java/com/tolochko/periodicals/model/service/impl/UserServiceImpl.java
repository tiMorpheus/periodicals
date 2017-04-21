package com.tolochko.periodicals.model.service.impl;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.UserService;
import org.apache.log4j.Logger;

import java.util.List;

import static java.util.Objects.nonNull;


public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private static final UserServiceImpl instance = new UserServiceImpl();
    private DaoFactory factory = MySqlDaoFactory.getFactoryInstance();


    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }


    @Override
    public User findOneById(long id) {
        User user = factory.getUserDao().findOneById(id);

        setUserRole(user);
        return user;
    }

    private void setUserRole(User user) {
        if (nonNull(user)) {
            logger.info("setting role to user: " + user.getUsername());

            user.setRole(factory.getRoleDao().findRoleByUserName(user.getUsername()));

            logger.debug(user);
        }
    }

    @Override
    public User findOneUserByUserName(String userName) {

        User user = factory.getUserDao().findOneByUserName(userName);

        setUserRole(user);

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> allUser = factory.getUserDao().findAll();
        allUser.forEach(user -> user.setRole(factory.getRoleDao()
                .findRoleByUserName(user.getUsername())));

        return allUser;
    }

    @Override
    public boolean createNewUser(User user) {
        try {

            //connection.beginTransaction();

            Long userId = factory.getUserDao().add(user);

            if (userId == 0) {
                //connection.rollbackTransaction();
                return false;
            }

            factory.getRoleDao().addRole(userId, User.Role.SUBSCRIBER);
           // connection.commitTransaction();

            return true;
        } catch (RuntimeException e) {
            logger.error("transaction failed", e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean emailExistsInDb(String email) {

        return factory.getUserDao().emailExistsInDb(email);
    }


}

package com.tolochko.periodicals.model.service.impl;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import com.tolochko.periodicals.model.domain.user.User;
import com.tolochko.periodicals.model.service.UserService;
import org.apache.log4j.Logger;

import java.util.List;

import static java.util.Objects.nonNull;


public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    private static final UserServiceImpl instance = new UserServiceImpl();
    private DaoFactory factory = MySqlDaoFactory.getFactoryInstance();
    private ConnectionPool pool = ConnectionPoolProvider.getPool();


    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }


    @Override
    public User findOneById(long id) {
        try (AbstractConnection connection = pool.getConnection()) {
            User user = factory.getUserDao(connection).findOneById(id);

            setUserRole(user, connection);

            return user;
        }
    }

    private void setUserRole(User user, AbstractConnection conn) {
        if (nonNull(user)) {
            logger.info("setting role to user: " + user.getUsername());

            user.setRole(factory.getRoleDao(conn).findRoleByUserName(user.getUsername()));

            logger.debug(user);
        }
    }

    @Override
    public User findOneUserByUserName(String userName) {

        try (AbstractConnection conn = pool.getConnection()) {
            logger.debug("try to find user: " + userName);

            User user = factory.getUserDao(conn).findOneByUserName(userName);
            logger.debug(user);

            setUserRole(user, conn);
            logger.debug(user);

            return user;
        }
    }

    @Override
    public List<User> findAll() {
        try (AbstractConnection conn = pool.getConnection()) {
            List<User> allUser = factory.getUserDao(conn).findAll();
            allUser.forEach(user -> user.setRole(factory.getRoleDao(conn)
                    .findRoleByUserName(user.getUsername())));

            return allUser;
        }
    }

    @Override
    public boolean createNewUser(User user) {
        try (AbstractConnection connection = pool.getConnection()) {

            connection.beginTransaction();

            Long userId = factory.getUserDao(connection).add(user);

            if (userId == 0) {
                connection.rollbackTransaction();
                return false;
            }

            factory.getRoleDao(connection).addRole(userId, User.Role.SUBSCRIBER);
            connection.commitTransaction();

            return true;
        } catch (RuntimeException e) {
            logger.error("transaction failed", e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean emailExistsInDb(String email) {
        try (AbstractConnection conn = pool.getConnection()) {
            return factory.getUserDao(conn).emailExistsInDb(email);
        }
    }


}

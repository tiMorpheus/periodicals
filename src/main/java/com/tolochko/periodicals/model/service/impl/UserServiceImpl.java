package com.tolochko.periodicals.model.service.impl;

import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.service.UserService;
import org.apache.log4j.Logger;

public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
    private DaoFactory factory = MySqlDaoFactory.getFactoryInstance();

    private static UserService userService = new UserServiceImpl();

    public UserServiceImpl() {
        this.factory = factory;
    }
}

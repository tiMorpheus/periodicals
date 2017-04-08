package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.dao.GenericDAO;
import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.domain.user.User;

public interface UserDao extends GenericDAO<User>{

    User findByFirstName(String firstName) throws DaoException;

    User findUserByEmail(String email);

    /**
     * For registration new users
     * user cannot create account if email is used
     */
    boolean isEmailExistsInDb(String email) throws DaoException;

    int deleteUserFromDb(String email);
}

package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.domain.user.User;

public interface UserDao extends GenericDao<User, Long> {

    /**
     *  Return user by email
     *
     * @param email
     * @return User
     */
    User findUserByEmail(String email);

    Long findRoleId(User.Role role);

    User.Role readRole(Long id);
}

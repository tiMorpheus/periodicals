package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.domain.user.User;

import java.util.Set;

public interface RoleDao {

    /**
     * Roles define specific system functionality available to a user.
     *
     * @param username
     * @return Set of roles
     */
    Set<User.Role> findRolesByUsername(String username);

    /**
     * Add some role to user
     *
     * @param userId
     * @param role
     */
    void addRole(long userId, User.Role role);
    
}

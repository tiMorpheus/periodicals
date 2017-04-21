package com.tolochko.periodicals.model.dao.impl;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.dao.interfaces.RoleDao;
import com.tolochko.periodicals.model.domain.user.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class RoleDaoImpl implements RoleDao {
    private static final Logger logger = Logger.getLogger(RoleDaoImpl.class);

    private Connection connection;

    public RoleDaoImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public User.Role findRoleByUserName(String userName) {
        String query = "SELECT user_roles.name " +
                "FROM users INNER JOIN user_roles " +
                "ON (users.id = user_roles.user_id) " +
                "WHERE users.username = ?";


        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, userName);


            try (ResultSet rs = st.executeQuery()) {
               return rs.next() ? User.Role.valueOf(rs.getString("user_roles.name").toUpperCase()) : null;
            }

        } catch (SQLException e) {
            String message = String.format("Exception during retrieving roles for user with userName = '%s'", userName);
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }

    @Override
    public void addRole(long userId, User.Role role) {
        String sqlStatement = "INSERT INTO user_roles " +
                "(user_id, name) VALUES (?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sqlStatement)) {
            st.setLong(1, userId);
            st.setString(2, role.toString());

            st.executeUpdate();

        } catch (SQLException e) {
            String message = String.format("Exception during executing statement: '%s' for userId = %d", sqlStatement, userId);
            logger.error(message, e);
            throw new DaoException(message, e);
        }
    }
}

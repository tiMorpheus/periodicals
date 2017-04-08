package com.tolochko.periodicals.model.dao.util;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.domain.user.User;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    private static final Logger logger = Logger.getLogger(UserMapper.class);


    /**
     * Creates a new user using the data from the result set.
     */
    public static User map(ResultSet rs){
        User.Builder userBuilder = null;

        try {
            userBuilder = new User.Builder()
                    .setId(rs.getLong("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setEmail(rs.getString("email"))
                    .setAddress(rs.getString("address"))
                    .setPassword(rs.getString("password"));

            return userBuilder.build();
        } catch (SQLException e){
            logger.error("caught exception during mapping objects", e);
        }

        return null;
    }
}

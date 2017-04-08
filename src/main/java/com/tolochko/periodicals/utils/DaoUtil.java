package com.tolochko.periodicals.utils;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import com.tolochko.periodicals.model.domain.user.User;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.nonNull;

public final class DaoUtil {
    private static final Logger logger = Logger.getLogger(DaoUtil.class);

    private DaoUtil() {
    }

    /**
     * Creates a new periodical using the data from the result set.
     */
    public static Periodical getPeriodicalFromResultSet(ResultSet rs) throws DaoException {
        Periodical.Builder periodicalBuilder = null;

        try {
            periodicalBuilder = new Periodical.Builder()
                    .setId(rs.getLong("periodicals.id"))
                    .setName(rs.getString("periodicals.name"))
                    .setCategory(PeriodicalCategory.valueOf(
                            rs.getString("periodicals.category").toUpperCase()))
                    .setPublisher(rs.getString("periodicals.publisher"))
                    .setDescription(rs.getString("periodicals.description"))
                    .setOneMonthCost(rs.getLong("periodicals.one_month_cost"))
                    .setStatus(Periodical.Status.valueOf(
                            rs.getString("periodicals.status").toUpperCase()));

            return periodicalBuilder.build();
        } catch (SQLException e) {
            logger.error("I've caught error during building periodical for result set[DaoUtil]", e);
            throw new DaoException(e);
        }

    }

    /**
     * Creates a new user using the data from the result set.
     */
    public static User getUserFromResultSet(ResultSet rs) throws DaoException {
        User.Builder userBuilder = null;

        try {
            userBuilder = new User.Builder()
                    .setId(rs.getLong("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setEmail(rs.getString("email"))
                    .setAddress(rs.getString("address"))
                    .setPassword(rs.getString("password_hash"))
                    .setStatus(User.Status.valueOf(rs.getString("status").toUpperCase()));

            return userBuilder.build();
        } catch (Exception e) {
            logger.error("I've caught error during building periodical for result set", e);
            throw new DaoException(e);
        }
    }

}

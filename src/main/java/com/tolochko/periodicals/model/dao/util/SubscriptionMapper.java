package com.tolochko.periodicals.model.dao.util;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionMapper {
    private static final Logger logger = Logger.getLogger(SubscriptionMapper.class);


    /**
     * Creates a new subscription using the data from the result set.
     */
    public static Subscription map(ResultSet rs)  {
        Subscription.Builder subscriptionBuilder = null;

        try {
            subscriptionBuilder = new Subscription.Builder()
                    .setId(rs.getLong("id"))
                    .setUserId(rs.getLong("user_id"))
                    .setPeriodicalId(rs.getLong("periodical_id"))
                    .setDeliveryAddress(rs.getString("delivery_address"))
                    .setEndDate(rs.getTimestamp("end_date").toLocalDateTime())
                    .setStatus(Subscription.Status.valueOf(
                            rs.getString("status").toUpperCase()));

            return subscriptionBuilder.build();
        } catch (SQLException e) {
            logger.error("I've caught error during building subscription for result set[DaoUtil]", e);
            throw new DaoException(e);
        }

    }
}

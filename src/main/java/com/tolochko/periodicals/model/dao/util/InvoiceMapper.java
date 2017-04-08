package com.tolochko.periodicals.model.dao.util;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.domain.invoice.Invoice;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceMapper {

    private static final Logger logger = Logger.getLogger(InvoiceMapper.class);

    /**
     * Creates a new invoice using the data from the result set.
     */
    public static Invoice map(ResultSet rs)  {
        Invoice.Builder builder = null;

        try {
            builder = new Invoice.Builder()
                    .setId(rs.getLong("id"))
                    .setUserId(rs.getLong("user_id"))
                    .setPeriodicalId(rs.getLong("periodical_id"))
                    .setSubscriptionPeriod(rs.getInt("period"))
                    .setTotalSum(rs.getLong("total_sum"))
                    .setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                    .setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime())
                    .setStatus(Invoice.Status.valueOf(rs.getString("status").toUpperCase()));

            return builder.build();
        } catch (SQLException e) {
            logger.error("I've caught error during mapping invoice for result set", e);
            throw new DaoException(e);
        }

    }
}

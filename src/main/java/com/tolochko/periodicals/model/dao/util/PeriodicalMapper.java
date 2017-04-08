package com.tolochko.periodicals.model.dao.util;

import com.tolochko.periodicals.model.dao.exception.DaoException;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeriodicalMapper {
    private static final Logger logger = Logger.getLogger(PeriodicalMapper.class);

    /**
     * Creates a new periodical using the data from the result set.
     */
    public static Periodical map(ResultSet rs)  {
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
            logger.error("I've caught error during mapping periodical for result set", e);
            throw new DaoException(e);
        }

    }
}

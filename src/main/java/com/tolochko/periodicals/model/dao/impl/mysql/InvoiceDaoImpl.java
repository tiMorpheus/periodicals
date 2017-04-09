package com.tolochko.periodicals.model.dao.impl.mysql;

import com.tolochko.periodicals.model.dao.impl.mysql.helper.JdbcTemplates;
import com.tolochko.periodicals.model.dao.interfaces.InvoiceDao;
import com.tolochko.periodicals.model.dao.util.InvoiceMapper;
import com.tolochko.periodicals.model.domain.invoice.Invoice;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDaoImpl implements InvoiceDao {
    private static final Logger logger = Logger.getLogger(InvoiceDaoImpl.class);
    private JdbcTemplates templates = new JdbcTemplates();



    @Override
    public Long getCreatedInvoiceSumBy(LocalDateTime since, LocalDateTime until) {
        String query = "SELECT SUM(total_sum) FROM invoices " +
                "WHERE creation_date >= ? AND creation_date <= ?";
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = ConnectionPoolProvider.getConnection();
            ps = connection.prepareStatement(query);

            ps.setTimestamp(1, Timestamp.valueOf(since));
            ps.setTimestamp(2, Timestamp.valueOf(until));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }

        } catch (SQLException e){
            logger.error("Exception during creating total sum", e);
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                logger.error("Cannot close prepared statement", e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Cannot close connection!!!", e);
            }
        }

        return null;
    }

    @Override
    public Long getPaidInvoiceSumByPaymentDate(LocalDateTime since, LocalDateTime until) {
        String query = "SELECT SUM(total_sum) FROM invoices " +
                "WHERE payment_date >= ? AND payment_date <= ? AND status = ?";
        Connection connection = null;
        PreparedStatement ps = null;


        try {
            connection = ConnectionPoolProvider.getConnection();

            ps = connection.prepareStatement(query);

            ps.setTimestamp(1, Timestamp.valueOf(since));
            ps.setTimestamp(2, Timestamp.valueOf(until));
            ps.setString(3, Invoice.Status.PAID.name().toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            logger.error("exception while getting invoice sum", e);

        } finally {

            try {
                ps.close();
            } catch (SQLException e) {
                logger.error("cannot close preparedStatement");
            }
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Cannot close connection!!!");
            }
        }

        return null;
    }

    @Override
    public List<Invoice> findAll() {
        String query = "SELECT * FROM invoices";

        return templates.findObjects(query, InvoiceMapper::map);


    }

    @Override
    public Invoice findOneById(Long id) {
        String query = "SELECT * FROM invoices WHERE id = ?";


        return templates.findObject(query, InvoiceMapper::map, id);
    }


    @Override
    public long add(Invoice invoice) {
        String query = "INSERT INTO invoices " +
                "(user_id, periodical_id, period, total_sum, creation_date, payment_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        return templates.insert(query,
                invoice.getUserId(),
                invoice.getPeriodicalId(),
                invoice.getSubscriptionPeriod(),
                invoice.getTotalSum(),
                Timestamp.valueOf(invoice.getCreationDate()),
                Timestamp.valueOf(invoice.getPaymentDate()),
                invoice.getStatus().name().toLowerCase());
    }

    @Override
    public void updateById(Long id, Invoice invoice) {
        String query = "UPDATE invoices " +
                "SET user_id=?, periodical_id=?, period=?, total_sum=?, creation_date=?, " +
                "payment_date=?, status=? WHERE id=?";

        templates.update(query,
                invoice.getUserId(),
                invoice.getPeriodicalId(),
                invoice.getSubscriptionPeriod(),
                invoice.getTotalSum(),
                Timestamp.valueOf(invoice.getCreationDate()),
                Timestamp.valueOf(invoice.getPaymentDate()),
                invoice.getStatus().name().toLowerCase(),
                id);
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM invoices WHERE id = ?";

        templates.remove(query, id);

    }
}

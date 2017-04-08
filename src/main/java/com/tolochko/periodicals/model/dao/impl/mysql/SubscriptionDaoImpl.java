package com.tolochko.periodicals.model.dao.impl.mysql;

import com.tolochko.periodicals.model.dao.impl.mysql.helper.JdbcTemplates;
import com.tolochko.periodicals.model.dao.interfaces.SubscriptionDao;
import com.tolochko.periodicals.model.dao.util.SubscriptionMapper;
import com.tolochko.periodicals.model.domain.subscription.Subscription;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class SubscriptionDaoImpl implements SubscriptionDao {
    private static final Logger logger = Logger.getLogger(SubscriptionDaoImpl.class);
    private JdbcTemplates templates = new JdbcTemplates();

    @Override
    public Subscription findByUserIdAndPeriodicalId(Long userId, Long periodicalId) {
        String query = "SELECT * FROM subscriptions WHERE user_id = ? AND periodical_id = ?";

        return templates.findObject(query,SubscriptionMapper::map, userId, periodicalId);
    }


    @Override
    public Subscription findOneById(Long id) {
        String query = "SELECT * FROM subscriptions WHERE id = ?";

        return templates.findObject(query, SubscriptionMapper::map, id);
    }

    @Override
    public List<Subscription> findAll() {
        String query = "SELECT * FROM subscriptions";


        return templates.findObjects(query, SubscriptionMapper::map);
    }

    @Override
    public long add(Subscription subscription) {
        String query = "INSERT INTO subscriptions (user_id, periodical_id, delivery_address, end_date, status) "+
                        "VALUES(?,?,?,?,?)";

        return templates.insert(query,
                        subscription.getUserId(),
                        subscription.getPeriodicalId(),
                        subscription.getDeliveryAddress(),
                        Timestamp.valueOf(subscription.getEndDate()),
                        subscription.getStatus().name().toLowerCase());
    }

    @Override
    public void updateById(Long id, Subscription subscription) {
        String query = "UPDATE subscriptions " +
                "SET user_id=?, " +
                "periodical_id=?, " +
                "delivery_address=?, " +
                "end_date=?, " +
                "status=? " +
                "WHERE id=?";

        templates.update(query,
                subscription.getUserId(),
                subscription.getPeriodicalId(),
                subscription.getDeliveryAddress(),
                Timestamp.valueOf(subscription.getEndDate()),
                subscription.getStatus().name().toLowerCase(),
                id);
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM subscriptions WHERE id = ?";

        templates.remove(query, id);

    }
}

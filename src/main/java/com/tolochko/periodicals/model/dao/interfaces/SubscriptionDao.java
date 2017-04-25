package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.domain.subscription.Subscription;
import com.tolochko.periodicals.model.domain.user.User;

import java.util.List;

public interface SubscriptionDao extends GenericDao<Subscription, Long> {

    Subscription findOneByUserIdAndPeriodicalId(long userId, long periodicalId);

    /**
     * Retrieves all the subscriptions (active and expired) of the specified user.
     */
    List<Subscription> findAllByUser(User user);

    List<Subscription> findAllByPeriodicalIdAndStatus(long periodicalId,
                                                      Subscription.Status status);

}

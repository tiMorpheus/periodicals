package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.domain.subscription.Subscription;

import java.util.List;

public interface SubscriptionDao extends GenericDao<Subscription, Long> {

    Subscription findByUserIdAndPeriodicalId(Long userId, Long periodicalId);

}

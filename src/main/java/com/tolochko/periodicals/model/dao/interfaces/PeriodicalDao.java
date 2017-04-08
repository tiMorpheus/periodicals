package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;

import java.util.List;

public interface PeriodicalDao extends GenericDao<Periodical, Long> {

    Periodical findPeriodicalByNameAndPublisher(String name, String publisher);

    List<Periodical> findAllByStatus(Periodical.Status status);

    List<Periodical> findAllByCategory(PeriodicalCategory category);

    /**
     * Updates a periodical and sets a new status 'discarded' only if there is no active subscriptions
     * of this periodical.
     */
    void updateAndSetDiscarded(Periodical periodical);

    /**
     * Deletes from the db all periodicals with status = 'discarded'.
     * @return the number of deleted periodicals.
     */
    int deleteAllDiscarded();
}

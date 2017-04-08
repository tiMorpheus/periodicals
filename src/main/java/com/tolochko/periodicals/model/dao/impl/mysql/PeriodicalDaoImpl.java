package com.tolochko.periodicals.model.dao.impl.mysql;

import com.tolochko.periodicals.model.dao.impl.mysql.helper.JdbcTemplates;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.dao.util.PeriodicalMapper;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.domain.periodical.PeriodicalCategory;
import org.apache.log4j.Logger;

import java.util.List;

public class PeriodicalDaoImpl implements PeriodicalDao{
    private static final Logger logger = Logger.getLogger(PeriodicalDaoImpl.class);
    private JdbcTemplates templates = new JdbcTemplates();

    @Override
    public Periodical findPeriodicalByNameAndPublisher(String name, String publisher) {
        String query = "SELECT * FROM periodicals WHERE name = ? AND publisher = ?";


        return templates.findObject(query, PeriodicalMapper::map, name, publisher);
    }

    @Override
    public List<Periodical> findAllByStatus(Periodical.Status status) {
        String query = "SELECT * FROM periodicals WHERE status = ?";

        return templates.findObjects(query, PeriodicalMapper::map, status.name().toLowerCase());

    }

    @Override
    public List<Periodical> findAllByCategory(PeriodicalCategory category) {
        String query = "SELECT * FROM periodicals WHERE category = ?";

        return templates.findObjects(query, PeriodicalMapper::map, category.name().toLowerCase());
    }


    @Override
    public void updateAndSetDiscarded(Periodical periodical) {
        Periodical temp = findPeriodicalByNameAndPublisher(periodical.getName(), periodical.getPublisher());
        temp.setStatus(Periodical.Status.DISCARDED);
        updateById(temp.getId(), temp);
    }

    @Override
    public int deleteAllDiscarded() {
        String query = "DELETE FROM periodicals WHERE status='discarded'";


        return templates.remove(query);
    }


    @Override
    public long add(Periodical periodical) {
        String query = "INSERT INTO periodicals "+
                        "(name, category, publisher, description, one_month_cost, status) "+
                        "VALUES (?,?,?,?,?,?)";

        return templates.insert(query,
                periodical.getName(),
                periodical.getCategory().name().toLowerCase(),
                periodical.getPublisher(),
                periodical.getDescription(),
                periodical.getOneMonthCost(),
                periodical.getStatus().name().toLowerCase());
    }

    @Override
    public Periodical findOneById(Long id) {
        String query = "SELECT * FROM periodicals WHERE id = ?";

        return templates.findObject(query, PeriodicalMapper::map, id);
    }

    @Override
    public List<Periodical> findAll() {
        String query = "SELECT * FROM periodicals";

        List<Periodical> periodicals = templates.findObjects(query, PeriodicalMapper::map);

        return periodicals;
    }


    @Override
    public void updateById(Long id, Periodical periodical) {
        String query = "UPDATE periodicals " +
                "SET name=?, " +
                "category=?, " +
                "publisher=?, " +
                "description=?, " +
                "one_month_cost=?, " +
                "status=? " +
                "WHERE id=?";

        templates.update(query,
                periodical.getName(),
                periodical.getCategory().name().toLowerCase(),
                periodical.getPublisher(),
                periodical.getDescription(),
                periodical.getOneMonthCost(),
                periodical.getStatus().name().toLowerCase(),
                id);

    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM periodicals WHERE id = ?";

        templates.remove(query,id);
    }
}

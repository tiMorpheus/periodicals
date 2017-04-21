package com.tolochko.periodicals.model.service;

import com.tolochko.periodicals.model.dao.connection.AbstractConnection;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.PeriodicalDao;
import com.tolochko.periodicals.model.dao.pool.ConnectionPool;
import com.tolochko.periodicals.model.domain.periodical.Periodical;
import com.tolochko.periodicals.model.service.impl.PeriodicalServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PeriodicalServiceImplTest {
    private static final long PERIODICAL_ID = 10;
    private static final String TEST_NAME = "Test name";
    private static final int NEW_PERIODICAL_ID = 11;
    @Mock
    private DaoFactory factory;
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private PeriodicalDao periodicalDao;
    @Mock
    private AbstractConnection conn;

    @InjectMocks
    private PeriodicalService periodicalService = PeriodicalServiceImpl.getInstance();
    private Periodical periodical;
    private Periodical newPeriodical;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        periodical = new Periodical();
        periodical.setName(TEST_NAME);

        newPeriodical = new Periodical();
        newPeriodical.setId(NEW_PERIODICAL_ID);

        when(connectionPool.getConnection()).thenReturn(conn);
        when(factory.getPeriodicalDao(conn)).thenReturn(periodicalDao);
    }

    @Test
    public void save_Should_CreateNewPeriodical_IfIdIsZero() throws Exception {
        periodical.setId(0);
        when(periodicalDao.findOneByName(TEST_NAME)).thenReturn(newPeriodical);

        assertEquals(NEW_PERIODICAL_ID, periodicalService.save(periodical).getId(), 0.1);

        verify(periodicalDao, times(1)).add(periodical);
        verify(periodicalDao, times(1)).findOneByName(TEST_NAME);

    }

    @Test(expected = NoSuchElementException.class)
    public void save_Should_ThrowExceptionIfIdIsNotZero_AndPeriodicalDoesNotExist() {
        periodical.setId(PERIODICAL_ID);
        when(periodicalDao.findOneById(PERIODICAL_ID)).thenReturn(null);

        periodicalService.save(periodical).getId();
    }

    @Test
    public void updateAndSetDiscarded() throws Exception {
        periodicalService.updateAndSetDiscarded(periodical);

        verify(periodicalDao).updateAndSetDiscarded(periodical);
    }

    @Test
    public void deleteAllDiscarded() throws Exception {
        periodicalService.deleteAllDiscarded();

        verify(periodicalDao).deleteAllDiscarded();
    }
}
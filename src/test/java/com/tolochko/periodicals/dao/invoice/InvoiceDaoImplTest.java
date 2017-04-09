package com.tolochko.periodicals.dao.invoice;

import com.tolochko.periodicals.init.EntityCreator;
import com.tolochko.periodicals.init.InitDB;
import com.tolochko.periodicals.model.dao.factory.DaoFactory;
import com.tolochko.periodicals.model.dao.factory.impl.MySqlDaoFactory;
import com.tolochko.periodicals.model.dao.interfaces.InvoiceDao;
import com.tolochko.periodicals.model.domain.invoice.Invoice;
import com.tolochko.periodicals.model.dao.pool.ConnectionPoolProvider;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InvoiceDaoImplTest {
    private static ConnectionPoolProvider pool;
    private static DaoFactory factory;
    private static InvoiceDao invoiceDao;
    private static Invoice expected;
    private static long id;

    @BeforeClass
    public static void setUp() {
        pool = new ConnectionPoolProvider(InitDB.getMySqlDS());
        factory = MySqlDaoFactory.getFactoryInstance();
        invoiceDao = factory.getInvoiceDao();

        expected = EntityCreator.createInvoice();
    }

    @Test
    public void addTest() {
        id = invoiceDao.add(expected);

        assertInvoiceData(invoiceDao.findOneById(id));
    }

    private void assertInvoiceData(Invoice actual) {

        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getPeriodicalId(), actual.getPeriodicalId());
        assertEquals(expected.getSubscriptionPeriod(), actual.getSubscriptionPeriod());
        assertEquals(expected.getTotalSum(), actual.getTotalSum());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    public void updateByIdTest() {
        id = invoiceDao.add(expected);
        assertEquals(10000, invoiceDao.findOneById(id).getTotalSum());
        expected.setTotalSum(20000);

        invoiceDao.updateById(id, expected);

        assertEquals(20000, invoiceDao.findOneById(id).getTotalSum());
    }

    @Test
    public void findAllTest() {
        id = invoiceDao.add(expected);
        Long id2 = invoiceDao.add(expected);
        Long id3 = invoiceDao.add(expected);

        List<Invoice> invoices = invoiceDao.findAll();


        assertEquals(3, invoices.size());

        invoiceDao.delete(id2);
        invoiceDao.delete(id3);
    }

    @Test
    public void getPaidInvoiceSumByPaymentDateTest() {
        id = invoiceDao.add(expected);
        Long id2 = invoiceDao.add(expected);

        LocalDateTime t1 = LocalDateTime.of(2017, 4, 8, 21, 19, 8);
        LocalDateTime t2 = LocalDateTime.of(2018, 1, 1, 0, 0);


        Long sum = invoiceDao.getPaidInvoiceSumByPaymentDate(t1, t2);

        assertEquals(expected.getTotalSum() * 2, sum, 0.1);

        invoiceDao.delete(id2);
    }

    @Test
    public void getCreatedInvoiceSumByTest() {
        id = invoiceDao.add(expected);
        Long id2 = invoiceDao.add(expected);
        LocalDateTime t1 = LocalDateTime.of(2017, 4, 8, 21, 19, 8);
        LocalDateTime t2 = LocalDateTime.of(2018, 1, 1, 0, 0);
        Long sum = invoiceDao.getCreatedInvoiceSumBy(t1, t2);

        assertEquals(expected.getTotalSum() * 2, sum, 0.1);

        invoiceDao.delete(id2);

    }

    @After
    public void tearDown() {
        invoiceDao.delete(id);
    }


}

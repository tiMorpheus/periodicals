package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.dao.GenericDAO;
import com.tolochko.periodicals.model.domain.invoice.Invoice;

import java.time.Instant;
import java.util.List;

public interface InvoiceDao extends GenericDAO<Invoice> {

    List<Invoice> findAllByUserId(long userId);

    List<Invoice> findAllByPeriodicalId(long periodicalId);

    /**
     * Returns the sum of all invoices that were created during the specified time period
     * regardless whether they have been paid or not.
     * @param since the beginning of the time period
     * @param until the end of the time period
     */
    long getCreatedInvoiceSumBy(Instant since, Instant until);


    /**
     * Returns the sum of all invoices that were paid during the specified time period
     * regardless when they were created.
     * @param since the beginning of the time period
     * @param until the end of the time period
     */
    long getPaidInvoiceSumByPaymentDate(Instant since, Instant until);
}

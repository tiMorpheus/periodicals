package com.tolochko.periodicals.model.dao.interfaces;

import com.tolochko.periodicals.model.domain.invoice.Invoice;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceDao extends GenericDao<Invoice, Long> {

    /**
     * Returns the sum of all invoices that were created during the specified time period
     * regardless whether they have been paid or not.
     * @param since the beginning of the time period
     * @param until the end of the time period
     */
    Long getCreatedInvoiceSumBy(LocalDateTime since, LocalDateTime until);


    /**
     * Returns the sum of all invoices that were paid during the specified time period
     * regardless when they were created.
     * @param since the beginning of the time period
     * @param until the end of the time period
     */
    Long getPaidInvoiceSumByPaymentDate(LocalDateTime since, LocalDateTime until);
}

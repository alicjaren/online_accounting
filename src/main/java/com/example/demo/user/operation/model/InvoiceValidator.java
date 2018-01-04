package com.example.demo.user.operation.model;

import com.example.demo.invoices.dao.InvoiceDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceValidator {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();


    public boolean isTradeInvoiceInDB(String invoiceNumber, String username){
        return invoiceDao.isTradeInvoiceInDB(invoiceNumber, username);
    }

    public boolean isPurchaseInvoiceInDB(String invoiceNumber, long partnerNIP){
        return invoiceDao.isPurchaseInvoiceInDB(invoiceNumber, partnerNIP);
    }
}

package com.example.demo;

import com.example.demo.invoices.dao.InvoiceDaoImpl;
import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class DBInvoicesTest {

    InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    RecordDaoImpl recordDao = new RecordDaoImpl();
    Logger logger = Logger.getLogger("");

    //@Test
    public void testAddTradeInvoice(){
        TradeRecord tradeRecord = recordDao.getTradeRecord("12/2017", "user1");
        Date dateOfIssue = new Calendar.Builder().setDate(2017, 11, 27).build().getTime();
        TradeInvoice tradeInvoice = new TradeInvoice("11a/2017", dateOfIssue,1234567890,
                "Młyn Ambroziak", "pszenica", 100.00, 0.00, 0.00,
                23.00, 0.00, 0.00, 123.00, tradeRecord);
        logger.info("\n\n\n\n\n\n\n\n Dodaję fakturę!!!!!!!11\n \n \n \n \n \n \n \n \n \n");
        assertTrue(invoiceDao.addTradeInvoice(tradeInvoice));
    }

    //@Test
    public void testAddPurchaseInvoice(){
        PurchaseRecord purchaseRecord = recordDao.getPurchaseRecord("11/2017");
        Date dateOfIssue = new Calendar.Builder().setDate(2017, 11, 30).build().getTime();
        PurchaseInvoice purchaseInvoice = new PurchaseInvoice("F123/11/2017", dateOfIssue, 1234567890,
        "Sklep ogrodniczy Stokrotka", "nasiona tymotki", 0.00, 0.00, 100.00, 0.00, 0.00, 5.00, 105.00,
                true, 0.00, 0.00, 0.00, purchaseRecord);
        assertTrue(invoiceDao.addPurchaseInvoice(purchaseInvoice));
    }
}

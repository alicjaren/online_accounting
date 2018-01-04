package com.example.demo.user.operation.model;

import com.example.demo.invoices.dao.InvoiceDaoImpl;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.TradeRecord;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

/*adding, updating, selecting and deleting invoices*/
public class UserOperation {

    private Logger logger = Logger.getLogger("Loggs from UserOperation");
    private InvoiceValidator validator = new InvoiceValidator();
    private RecordDaoImpl recordDao = new RecordDaoImpl();
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();

    public String addTradeInvoiceToDB(String username, String invoiceNumber, Date dateOfIssue, long tradePartnerNIP,
                                      String tradePartnerName, String dealingThingName, double net23, double net8,
                                      double net5, double vat23, double vat8, double vat5, double gross){

        LocalDate localDate = dateOfIssue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        String recordName = month + "/" + year; //must reckon tradeInvoice in the month of trade
        TradeRecord tradeRecord = recordDao.getTradeRecord(recordName,username);
        TradeInvoice tradeInvoice = new TradeInvoice(invoiceNumber, dateOfIssue, tradePartnerNIP, tradePartnerName,
                dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross, tradeRecord);
        if (validator.isTradeInvoiceInDB(tradeInvoice.getInvoiceNumber(),username)){
            logger.info("Such tradeInvoice: " + tradeInvoice.getInvoiceNumber() + " already exists in DB");
            return "Taka faktura sprzedaży istnieje już w systemie! Istnieje jedynie możliwość" +
                    " jej edycji lub usunięcia.";
        }
        if(invoiceDao.addTradeInvoice(tradeInvoice)){
            return "SUCCESS";
        }
        return "Przepraszamy, wystąpił błąd techniczny. Faktura nie została zapisana. Prosimy spróbować ponownie.";
    }
}

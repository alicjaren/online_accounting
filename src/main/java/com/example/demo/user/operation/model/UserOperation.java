package com.example.demo.user.operation.model;

import com.example.demo.invoices.dao.InvoiceDaoImpl;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.invoices.model.TradeInvoiceComparator;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.TradeRecord;

import javax.swing.text.TabableView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

/*adding, updating, selecting and deleting invoices*/
public class UserOperation {

    private Logger logger = Logger.getAnonymousLogger();
    private InvoiceValidator validator = new InvoiceValidator();
    private RecordDaoImpl recordDao = new RecordDaoImpl();
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();

    public String addTradeInvoiceToDB(String username, String invoiceNumber, String dateOfIssue, String tradePartnerNIP,
                                      String tradePartnerName, String dealingThingName, double net23, double net8,
                                      double net5, double vat23, double vat8, double vat5, double gross){

        if (dateOfIssue.contains("-")){
            String [] parts = dateOfIssue.split("-");
            dateOfIssue = parts[0] + "/" + parts[1] + "/" + parts[2];
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd").parse(dateOfIssue);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            int year = localDate.getYear();
            String recordName = month + "/" + year; //must reckon tradeInvoice in the month of trade
            TradeRecord tradeRecord = recordDao.getTradeRecord(recordName,username);
            TradeInvoice tradeInvoice = new TradeInvoice(invoiceNumber, date, Long.valueOf(tradePartnerNIP), tradePartnerName,
                dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross, tradeRecord);

            if (validator.isTradeInvoiceInDB(tradeInvoice.getInvoiceNumber(),username)){
                logger.info("Such tradeInvoice: " + tradeInvoice.getInvoiceNumber() + " already exists in DB");
                return "Taka faktura sprzedaży istnieje już w systemie! Istnieje jedynie możliwość" +
                    " jej edycji lub usunięcia.";
            }

            if(!validator.isValidDateOfTradeInvoice(localDate)){
                return "Faktury z taką datą wystawienia: " + localDate + " nie można rozliczyć! Proszę sprawdzić, " +
                        "czy data jest prawidłowa";
            }

            if(!validator.isValidValuesOfVAT(net5, vat5)){
                return "Błędne dane dla kwoty podatku 5%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(net8, vat8)){
                return "Błędne dane dla kwoty podatku 8%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(net23, vat23)){
                return "Błędne dane dla kwoty podatku 23%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(gross == 0){
                return "Nie można dodać do rejestru faktury z zerową wartością kwoty brutto!";
            }

            if(invoiceDao.addTradeInvoice(tradeInvoice)){
                return "SUCCESS";
            }

        }catch(ParseException pe){
            logger.info("Error by date parsing");
            return "Błędny format daty, wymagany: RRRR/MM/DD";
        }

        return "Przepraszamy, wystąpił błąd techniczny. Faktura nie została zapisana. Prosimy spróbować ponownie.";
    }


    /*leak of invoices -> return empty ArrayList
    error by connection with DB -> return null
    ok -> return ArrayList sorted by data of issue */
    public ArrayList<TradeInvoice> getTradeInvoices(String userName, String tradeRecordName){
        ArrayList<TradeInvoice> invoices = new ArrayList<>(invoiceDao.getTradeInvoices(userName, tradeRecordName));
        invoices.sort(new TradeInvoiceComparator());
        invoices.forEach(in -> logger.info("Faktura nr: " + in.getInvoiceNumber() + " data: " + in.getDateOfIssue().toString()));
        return invoices;
    }
}

package com.example.demo.user.operation.model;

import com.example.demo.invoices.dao.InvoiceDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class InvoiceValidator {

    //private final Logger log = LoggerFactory.getLogger(getClass());
    private java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();


    public boolean isTradeInvoiceInDB(String invoiceNumber, String username){
        return invoiceDao.isTradeInvoiceInDB(invoiceNumber, username);
    }


    //You must reckon trade invoice in the month of date of issue
    public boolean isValidDateOfTradeInvoice(LocalDate dateOfIssue){
        Date dateNow =  new Date();
        LocalDate localDateNow = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        logger.info("Date of issue: day: " + dateOfIssue.getDayOfMonth() + " month: " + dateOfIssue.getMonthValue() + " year: " + dateOfIssue.getYear());

        if (localDateNow.compareTo(dateOfIssue) < 0){
            logger.info("DateOfIssue is from the future!");
            return false;
        }

        int monthDifference = localDateNow.getMonthValue() - dateOfIssue.getMonthValue();
        int yearDifference = localDateNow.getYear() - dateOfIssue.getYear();
        logger.info("Month difference =  " + monthDifference + " year difference: " + yearDifference);

        if(localDateNow.getDayOfMonth() <= 25){ //can register trade invoice from this or last month
            if(localDateNow.getMonthValue() == 1){ //in January accept invoice from January or December
                logger.info("Invoice must be from January or December");
                return (monthDifference == 0 && yearDifference == 0) || (monthDifference == -11 && yearDifference == 1);
            }
            else{
                logger.info("Invoice must be from this or last month");
                return (monthDifference == 0 || monthDifference == 1) && yearDifference == 0;
            }
        }
        else{ //can register trade invoice only from this month
            logger.info("Invoice must be from this month");
            return monthDifference == 0 && yearDifference == 0;
        }
    }

    public boolean isValidValuesOfVAT(double net, double vat){
        if ((net == 0 && vat != 0) || (net != 0 && vat  == 0)){
            logger.info("Net value is zero but VAT value no or inversely!");
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isPurchaseInvoiceInDB(String invoiceNumber, long partnerNIP){
        return invoiceDao.isPurchaseInvoiceInDB(invoiceNumber, partnerNIP);
    }
}

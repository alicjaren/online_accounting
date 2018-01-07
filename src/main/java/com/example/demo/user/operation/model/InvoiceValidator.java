package com.example.demo.user.operation.model;

import com.example.demo.invoices.dao.InvoiceDaoImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class InvoiceValidator {

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



    //return name if reckoning is possible or information about error if isn't
    public String findNameOfRecordForPurchaseInvoice(LocalDate dateOfIssue){

        //<= 25th January now
        Date dateNow =  new Date();
        LocalDate localDateNow = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        //>= 25th February now
//        Date dateNow = new Calendar.Builder().setDate(2018, 2, 25).build().getTime();
//        LocalDate localDateNow = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (localDateNow.getMonthValue() == dateOfIssue.getMonthValue() && localDateNow.getYear() == dateOfIssue.getYear()){
            logger.info("Invoice from this month");
            return dateOfIssue.getMonthValue() + "/" + dateOfIssue.getYear();
        }

        if (localDateNow.compareTo(dateOfIssue) < 0){
            logger.info("DateOfIssue is from the future!");
            return "Faktura posiada datę wyprzedzającą dziesiejszy dzień. Jej rozliczenie nie jest możliwe.";
        }

        int yearDifference = localDateNow.getYear() - dateOfIssue.getYear();
        int monthNow = localDateNow.getMonthValue();
        int monthInvoice = dateOfIssue.getMonthValue();
        int monthDifference = monthNow - monthInvoice;

        if (yearDifference > 1){
            logger.info("To old purchase reckon");
            return "Nie można rozliczyć podanej faktury, jest ona starsza niż rok.";
        }

        //invoice from previous months
        if(localDateNow.getDayOfMonth() <= 25){//let to reckon invoice from last 3 months to last month reckon

            if (monthNow == 1){ //<= 25th January now -> invoice from December, November or October

                if ((monthDifference == -11 || monthDifference == -10 || monthDifference == -9) && yearDifference == 1){
                    logger.info("Purchase invoice from last 3 months to reckon in the last month");
                    return "12/" + dateOfIssue.getYear();
                }
                else{
                    logger.info("PurchaseInvoice is older than 3 months.");
                    return "Faktura została wystawiona ponad 3 miesiące temu, nie można już jej rozliczyć.";
                }
            }

            else{
               if((monthNow == 2 && ((monthDifference == 1 && yearDifference == 0)|| ((monthDifference == -10 || monthDifference == -9) && yearDifference == 1)))
               || (monthNow == 3 && (((monthDifference == 1 || monthDifference == 2) && yearDifference == 0) || (monthDifference  == -9 && yearDifference == 1)))
               || ((monthDifference == 1 || monthDifference == 2 || monthDifference == 3) && yearDifference == 0)){
                   logger.info("Purchase invoice from last 3 months to reckon in the last month");
                   return monthNow-1 + "/" + localDateNow.getYear();
                }
                else{
                   logger.info("PurchaseInvoice is older than 3 months.");
                   return "Faktura została wystawiona ponad 3 miesiące temu, nie można już jej rozliczyć.";
               }
            }
        }

        else{//>=25th -> let to reckon invoice from last two months in this month reckoning
            if((monthNow == 1 && (monthDifference == -11 || monthDifference == -10)  && yearDifference  == 1)
                    || (monthNow == 2 && ((monthDifference == -10 && yearDifference == 1) || (monthDifference == 1 && yearDifference == 0)))
                    || ((monthDifference == 1 || monthDifference == 2) && yearDifference == 0)){
                logger.info("Purchase invoice from last 2 months to reckon in this month");
                return monthNow + "/" + localDateNow.getYear();
            }
            else{
                logger.info("PurchaseInvoice is older than 3 months.");
                return "Faktura została wystawiona ponad 3 miesiące temu, nie można już jej rozliczyć.";
            }
        }
    }

    public boolean isPurchaseInvoiceInDB(String invoiceNumber, String partnerNIP){
        return invoiceDao.isPurchaseInvoiceInDB(invoiceNumber, Long.valueOf(partnerNIP));
    }
}

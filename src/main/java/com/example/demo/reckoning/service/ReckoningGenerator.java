package com.example.demo.reckoning.service;

import com.example.demo.invoices.dao.InvoiceDaoImpl;
import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.dao.MonthlyReckoningDaoImpl;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.service.DBOperations;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;

import java.util.List;
import java.util.logging.Logger;

/*generate monthly reckoning from data of records (trade record and purchase record)*/
public class ReckoningGenerator {

    private RecordDaoImpl recordDao = new RecordDaoImpl();
    private Logger logger = Logger.getAnonymousLogger();
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    private DBOperations dbOperations = new DBOperations();

    public boolean generateReckoning(String userName, String reckoningName) {

        //if one of records doesn't exist, other too, they are created all in one operation
        if(!recordDao.isRecordInDBByName(reckoningName, userName, true)){
            logger.info("Records didn't exist i DB, I create them. All sums are equals to 0");
            return createRecords(userName, reckoningName);
        }

        //counting tradeRecord
        TradeRecord tradeRecord = recordDao.getTradeRecord(reckoningName, userName);
        List<TradeInvoice> tradeInvoices = invoiceDao.getTradeInvoices(userName, reckoningName);
        if(!countAndUpdateTradeRecord(tradeRecord, tradeInvoices)){
            logger.info("Error by updating tradeRecords");
            return false;
        }

        //counting purchaseRecord
        PurchaseRecord purchaseRecord = recordDao.getPurchaseRecord(reckoningName, userName);
        List<PurchaseInvoice> purchaseInvoices = invoiceDao.getPurchaseInvoices(userName, reckoningName);
        if(!countAndUpdatePurchaseRecord(purchaseRecord, purchaseInvoices)){
            logger.info("Error by updating purchaseRecord");
            return false;
        }

        //counting monthlyReckoning
        return true;
    }

    //create tradeRecord, purchaseRecord and Monthly Reckoning
    public boolean createRecords(String username, String recordName){

        TradeRecord tradeRecord = new TradeRecord(recordName);
        PurchaseRecord purchaseRecord = new PurchaseRecord(recordName);
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.getUser(username);
        MonthlyReckoning newMonthlyReckoning = new MonthlyReckoning(recordName,0,0,
                0, 0, 0, user, tradeRecord, purchaseRecord);
        MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();

        if(!recordDao.addPurchaseRecord(purchaseRecord) || !recordDao.addTradeRecord(tradeRecord)){
            logger.info("Error by creating new tradeRecord and new PurchaseRecord");
            return false;
        }
        if (!monthlyReckoningDao.addMonthlyReckoning(newMonthlyReckoning)){
            logger.info("Error by creating new monthlyReckoning!");
            return false;
        }
        return true;
    }

    private boolean countAndUpdateTradeRecord(TradeRecord tradeRecord, List<TradeInvoice> tradeInvoices){

        double sumNet23 = 0;
        double sumVat23 = 0;
        double sumNet8 = 0;
        double sumVat8 = 0;
        double sumNet5 = 0;
        double sumVat5 = 0;
        double sumGross = 0;

        for (TradeInvoice invoice : tradeInvoices) {
            sumNet23 += invoice.getNet23();
            sumVat23 += invoice.getVat23();
            sumNet8 += invoice.getNet8();
            sumVat8 += invoice.getVat8();
            sumNet5 += invoice.getNet5();
            sumVat5 += invoice.getVat5();
            sumGross += invoice.getGross();
        }

        tradeRecord.setSumNet23(sumNet23);
        tradeRecord.setSumVat23(sumVat23);
        tradeRecord.setSumNet8(sumNet8);
        tradeRecord.setSumVat8(sumVat8);
        tradeRecord.setSumNet5(sumNet5);
        tradeRecord.setSumVat5(sumVat5);
        tradeRecord.setSumGross(sumGross);

        return dbOperations.updateInDB(tradeRecord);
    }

    private boolean countAndUpdatePurchaseRecord(PurchaseRecord purchaseRecord, List<PurchaseInvoice> purchaseInvoices){

        double sumNet23 = 0;
        double sumVat23 = 0;
        double sumNet8 = 0;
        double sumVat8 = 0;
        double sumNet5 = 0;
        double sumVat5 = 0;
        double sumGross = 0;
        double sumFixedAssetsNet = 0;
        double sumFixedAssetsVat = 0;
        double sumFixedAssetsGross = 0;

        for (PurchaseInvoice invoice : purchaseInvoices) {
            if(invoice.isDeducted()){
                sumNet23 += invoice.getNet23();
                sumVat23 += invoice.getVat23();
                sumNet8 += invoice.getNet8();
                sumVat8 += invoice.getVat8();
                sumNet5 += invoice.getNet5();
                sumVat5 += invoice.getVat5();
                sumGross += invoice.getGross();
                sumFixedAssetsNet += invoice.getFixedAssetsNet();
                sumFixedAssetsVat += invoice.getFixedAssetsVat();
                sumFixedAssetsGross += invoice.getFixedAssetsGross();
            }

            purchaseRecord.setSumNet23(sumNet23);
            purchaseRecord.setSumVat23(sumVat23);
            purchaseRecord.setSumNet8(sumNet8);
            purchaseRecord.setSumVat8(sumVat8);
            purchaseRecord.setSumNet5(sumNet5);
            purchaseRecord.setSumVat5(sumVat5);
            purchaseRecord.setSumGross(sumGross);
            purchaseRecord.setSumFixedAssetsNet(sumFixedAssetsNet);
            purchaseRecord.setSumFixedAssetsVat(sumFixedAssetsVat);
            purchaseRecord.setSumFixedAssetsGross(sumFixedAssetsGross);

        }

        return dbOperations.updateInDB(purchaseRecord);
    }
}

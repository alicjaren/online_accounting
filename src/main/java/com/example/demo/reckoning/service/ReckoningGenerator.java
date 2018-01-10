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

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/*generate monthly reckoning from data of records (trade record and purchase record)*/
public class ReckoningGenerator {

    private RecordDaoImpl recordDao = new RecordDaoImpl();
    private Logger logger = Logger.getAnonymousLogger();
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    private DBOperations dbOperations = new DBOperations();
    private MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();

    public Map<String, Integer> generateReckoning(String userName, String reckoningName) {

        //if one of records doesn't exist, other too, they are created all in one operation
        if(!recordDao.isRecordInDBByName(reckoningName, userName, true)){
            logger.info("Records didn't exist i DB, I create them. All sums are equals to 0");
            if(!createRecords(userName, reckoningName)){
                logger.info("Error by creating records");
                return null;
            }
        }

        //counting tradeRecord
        TradeRecord tradeRecord = recordDao.getTradeRecord(reckoningName, userName);
        List<TradeInvoice> tradeInvoices = invoiceDao.getTradeInvoices(userName, reckoningName);
        if(!countAndUpdateTradeRecord(tradeRecord, tradeInvoices)){
            logger.info("Error by updating tradeRecords");
            return null;
        }

        //counting purchaseRecord
        PurchaseRecord purchaseRecord = recordDao.getPurchaseRecord(reckoningName, userName);
        List<PurchaseInvoice> purchaseInvoices = invoiceDao.getPurchaseInvoices(userName, reckoningName);
        if(!countAndUpdatePurchaseRecord(purchaseRecord, purchaseInvoices)){
            logger.info("Error by updating purchaseRecord");
            return null;
        }

        //counting monthlyReckoning
        Map<String, Integer> preDeclaration = createPreMonthlyReckoningMap(userName, reckoningName);
        if (preDeclaration == null){
            return null;
        }
        MonthlyReckoning monthlyReckoning = monthlyReckoningDao.getMonthlyReckoning(userName, reckoningName);

        //tax for revenue greater than tax for client -> update monthlyReckoning, final declaration is done
        if(preDeclaration.get("overhang") == 0){
            if(updateMonthlyReckoning(monthlyReckoning, preDeclaration)){
                logger.info("tax for revenue greater than tax for client -> update monthlyReckoning," +
                        " final declaration is done");
                return preDeclaration;
            }
            else{
                logger.info("Error by updating monthly reckoning");
                return null;
            }
        }

        return preDeclaration;
    }

    //create tradeRecord, purchaseRecord and Monthly Reckoning
    public boolean createRecords(String username, String recordName){

        TradeRecord tradeRecord = new TradeRecord(recordName);
        PurchaseRecord purchaseRecord = new PurchaseRecord(recordName);
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.getUser(username);
        MonthlyReckoning newMonthlyReckoning = new MonthlyReckoning(recordName,0,0,
                0, 0, 0, user, tradeRecord, purchaseRecord);

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

    /*create map; key-name of filed, Integer - value of field from reckoning*/
    private Map<String, Integer> createPreMonthlyReckoningMap(String username, String reckoningName){

        MonthlyReckoning monthlyReckoning = monthlyReckoningDao.getMonthlyReckoning(username, reckoningName);
        TradeRecord tradeRecord = recordDao.getTradeRecord(reckoningName, username);
        PurchaseRecord purchaseRecord = recordDao.getPurchaseRecord(reckoningName, username);

        if(tradeRecord == null || purchaseRecord == null){ //error by connection with DB
            return null;
        }

        Map<String, Integer> preMonthlyReckoningMap = new HashMap<>();

        //tax due
        preMonthlyReckoningMap.put("sumTradeNet5",(int) Math.round(tradeRecord.getSumNet5()));
        preMonthlyReckoningMap.put("sumTradeVat5", (int) Math.round(tradeRecord.getSumVat5()));
        preMonthlyReckoningMap.put("sumTradeNet8", (int) Math.round(tradeRecord.getSumNet8()));
        preMonthlyReckoningMap.put("sumTradeVat8", (int) Math.round(tradeRecord.getSumVat8()));
        preMonthlyReckoningMap.put("sumTradeNet23", (int) Math.round(tradeRecord.getSumNet23()));
        preMonthlyReckoningMap.put("sumTradeVat23", (int) Math.round(tradeRecord.getSumVat23()));

        preMonthlyReckoningMap.put("sumTradeNet", preMonthlyReckoningMap.get("sumTradeNet5") +
                preMonthlyReckoningMap.get("sumTradeNet8") + preMonthlyReckoningMap.get("sumTradeNet23"));

        preMonthlyReckoningMap.put("sumTradeVat", preMonthlyReckoningMap.get("sumTradeVat5") +
                preMonthlyReckoningMap.get("sumTradeVat8") + preMonthlyReckoningMap.get("sumTradeVat23"));


        //input tax
        String previousReckoningName = monthlyReckoningDao.getPreviousReckoningName(reckoningName);
        MonthlyReckoning previousMonthlyReckoning = monthlyReckoningDao.getMonthlyReckoning(username, previousReckoningName);
        preMonthlyReckoningMap.put("fromPreviousMonth", previousMonthlyReckoning.getForNextMonth());

        preMonthlyReckoningMap.put("sumFixedAssetsNet", (int) Math.round(purchaseRecord.getSumFixedAssetsNet()));
        preMonthlyReckoningMap.put("sumFixedAssetsVat", (int) Math.round(purchaseRecord.getSumFixedAssetsVat()));

        preMonthlyReckoningMap.put("sumPurchaseOthersNet", (int) Math.round(purchaseRecord.getSumNet5()
                + purchaseRecord.getSumNet8() + purchaseRecord.getSumNet23()));
        preMonthlyReckoningMap.put("sumPurchaseOthersVat", (int) Math.round(purchaseRecord.getSumVat5()
                + purchaseRecord.getSumVat8() + purchaseRecord.getSumVat23()));

        preMonthlyReckoningMap.put("sumPurchaseVat", preMonthlyReckoningMap.get("fromPreviousMonth")
                + preMonthlyReckoningMap.get("sumFixedAssetsVat") + preMonthlyReckoningMap.get("sumPurchaseOthersVat"));

        Integer overhang = preMonthlyReckoningMap.get("sumPurchaseVat") - preMonthlyReckoningMap.get("sumTradeVat");
        if(overhang < 0){ //tax for revenue
            preMonthlyReckoningMap.put("forRevenue", Math.abs(overhang));
            preMonthlyReckoningMap.put("overhang", 0);
        }
        else{//tax for client
            preMonthlyReckoningMap.put("forRevenue", 0);
            preMonthlyReckoningMap.put("overhang", overhang);
        }

        //field for monthly reckoning
        preMonthlyReckoningMap.put("sumRefund", 0);
        preMonthlyReckoningMap.put("refund25Days", 0);
        preMonthlyReckoningMap.put("refund60Days", 0);
        preMonthlyReckoningMap.put("refund180Days", 0);
        preMonthlyReckoningMap.put("forNextMonth", 0);

        return preMonthlyReckoningMap;
    }

    private boolean updateMonthlyReckoning(MonthlyReckoning monthlyReckoning, Map<String, Integer> declaration){

        monthlyReckoning.setForNextMonth(declaration.get("forNextMonth"));
        monthlyReckoning.setForRevenue(declaration.get("forRevenue"));
        monthlyReckoning.setRefund25Days(declaration.get("refund25Days"));
        monthlyReckoning.setRefund60Days(declaration.get("refund60Days"));
        monthlyReckoning.setRefund180Days(declaration.get("refund180Days"));

        return dbOperations.updateInDB(monthlyReckoning);
    }

}

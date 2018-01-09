package com.example.demo.reckoning.dao;

import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.service.DBOperations;

import java.util.logging.Logger;

public class RecordDaoImpl implements RecordDao {

    Logger logger = Logger.getLogger("");
    DBOperations DBOperations = new DBOperations();


    @Override
    public boolean isRecordInDBByName(String recordName, String userName, boolean tradeRecord){

        if(tradeRecord){
            return getTradeRecord(recordName,userName) != null;
        }
        else{
             return getPurchaseRecord(recordName, userName) != null;
        }
    }


    @Override
    public boolean addTradeRecord(TradeRecord tradeRecord) {

        return DBOperations.addToDB(tradeRecord);
    }

    @Override
    public boolean addPurchaseRecord(PurchaseRecord purchaseRecord) {

        return DBOperations.addToDB(purchaseRecord);

    }


    @Override
    public TradeRecord getTradeRecord(String recordName, String userName){
        MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();
        MonthlyReckoning monthlyReckoning = monthlyReckoningDao.getMonthlyReckoning(userName, recordName);
        if (monthlyReckoning == null){
            return null;
        }
        return monthlyReckoning.getTradeRecord();
    }

    @Override
    public PurchaseRecord getPurchaseRecord(String recordName, String userName) {
        MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();
        MonthlyReckoning monthlyReckoning = monthlyReckoningDao.getMonthlyReckoning(userName, recordName);
        if (monthlyReckoning == null){
            return null;
        }
        return monthlyReckoning.getPurchaseRecord();
    }
}

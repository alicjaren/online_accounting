package com.example.demo.reckoning.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.service.AddingToDB;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RecordDaoImpl implements RecordDao {

    Logger logger = Logger.getLogger("");
    AddingToDB addingToDB = new AddingToDB();


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

        return addingToDB.addToDB(tradeRecord);
    }

    @Override
    public boolean addPurchaseRecord(PurchaseRecord purchaseRecord) {

        return addingToDB.addToDB(purchaseRecord);

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

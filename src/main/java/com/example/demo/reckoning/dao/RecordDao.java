package com.example.demo.reckoning.dao;

import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;

public interface RecordDao {

    boolean isRecordInDB(int recordId, boolean tradeRecord);

    boolean isRecordInDBByName(String name, boolean tradeRecord);

    boolean addTradeRecord(TradeRecord tradeRecord);

    boolean addPurchaseRecord(PurchaseRecord purchaseRecord);

    TradeRecord getTradeRecord(String name, String username);

    PurchaseRecord getPurchaseRecord(String name);

}

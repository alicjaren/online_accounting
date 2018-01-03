package com.example.demo;

import com.example.demo.reckoning.dao.MonthlyReckoningDaoImpl;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DBReckoningTest {

    RecordDaoImpl recordDao = new RecordDaoImpl();
    MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();
    Logger logger = Logger.getLogger("");

    @Test
    public void testAddRecords(){

        TradeRecord tradeRecord = new TradeRecord("12/2017", 100.00, 0, 0 ,
                23.00, 0, 0, 123.00 );
        PurchaseRecord purchaseRecord = new PurchaseRecord("12/2017", 10.00, 0, 0 ,
                2.30, 0, 0, 12.30 );
        PurchaseRecord purchaseRecord2 = new PurchaseRecord("11/2017", 20.00, 0, 0 ,
                4.60, 0, 0, 24.60 );
        assertFalse(recordDao.addTradeRecord(tradeRecord)); //such record already exists
        assertFalse(recordDao.addPurchaseRecord(purchaseRecord)); //such record already exists
        assertFalse(recordDao.addPurchaseRecord(purchaseRecord2)); //such record already exists

        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.getUser("user1");
        MonthlyReckoning monthlyReckoning = new MonthlyReckoning("12/2017", 20, 0,
                0, 0, 0, user, tradeRecord, purchaseRecord);
        assertFalse(monthlyReckoningDao.addMonthlyReckoning(monthlyReckoning)); //such reckoning already exists
    }

}

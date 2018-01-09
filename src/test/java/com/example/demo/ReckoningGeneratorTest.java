package com.example.demo;

import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.reckoning.service.ReckoningGenerator;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ReckoningGeneratorTest {
    private ReckoningGenerator reckoningGenerator = new ReckoningGenerator();
    private RecordDaoImpl recordDao = new RecordDaoImpl();

   // @Test
    public void testCreateReckoning(){
        assertTrue(reckoningGenerator.generateReckoning("user1", "9/2017"));
        assertFalse(reckoningGenerator.generateReckoning("user1", "9/2017"));
    }

    //@Test
    public void testCreateAndUpdateTradeRecord(){
        assertTrue(reckoningGenerator.generateReckoning("user1", "1/2018"));
        TradeRecord tradeRecord =  recordDao.getTradeRecord("1/2018", "user1");
        assertEquals(100.00, tradeRecord.getSumNet23());
        assertEquals(1000.00, tradeRecord.getSumNet8());
        assertEquals(3000.50, tradeRecord.getSumNet5());
        assertEquals(23.00, tradeRecord.getSumVat23());
        assertEquals(230.00, tradeRecord.getSumVat8());
        assertEquals(150.03, tradeRecord.getSumVat5());
        assertEquals(4503.53, tradeRecord.getSumGross());
    }

   // @Test
    public void testCreateAndUpdatePurchaseRecord(){
        assertTrue(reckoningGenerator.generateReckoning("user1", "1/2018"));
        PurchaseRecord purchaseRecord = recordDao.getPurchaseRecord("1/2018", "user1");
        assertEquals(2100.00, purchaseRecord.getSumNet23());
        assertEquals(483.00, purchaseRecord.getSumVat23());
        assertEquals(200.0, purchaseRecord.getSumNet8());
        assertEquals(16.0, purchaseRecord.getSumVat8());
        assertEquals(100.0, purchaseRecord.getSumNet5());
        assertEquals(5.00, purchaseRecord.getSumVat5());
        assertEquals(2904.0, purchaseRecord.getSumGross());
        assertEquals(60000.0, purchaseRecord.getSumFixedAssetsNet());
        assertEquals(13800.0, purchaseRecord.getSumFixedAssetsVat());
        assertEquals(73800.0,purchaseRecord.getSumFixedAssetsGross());
    }
}

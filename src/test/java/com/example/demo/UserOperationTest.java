package com.example.demo;

import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.user.operation.model.UserOperation;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UserOperationTest {
    private UserOperation userOperation = new UserOperation();
    private RecordDaoImpl recordDao = new RecordDaoImpl();

    @Test
    public void testAddTradeInvoiceToDB(){
        Date dateOfIssue = new Calendar.Builder().setDate(2017, 11, 27).build().getTime();
        assertEquals(userOperation.addTradeInvoiceToDB("user1", "11a/2017",dateOfIssue,1234567890,
                "Młyn Ambroziak", "pszenica", 100.00, 0.00, 0.00,
                23.00, 0.00, 0.00, 123.00 ), "Taka faktura sprzedaży istnieje już w systemie! " +
                "Istnieje jedynie możliwość jej edycji lub usunięcia.");
        assertEquals(userOperation.addTradeInvoiceToDB("user1", "Fv11b/2017",dateOfIssue,1234567890,
                "Dymek", "rzepak", 100.00, 0.00, 0.00,
                23.00, 0.00, 0.00, 123.00 ), "Taka faktura sprzedaży istnieje już w systemie! " +
                "Istnieje jedynie możliwość jej edycji lub usunięcia.");
    }
}

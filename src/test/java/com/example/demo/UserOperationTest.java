package com.example.demo;

import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.user.operation.model.UserOperation;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UserOperationTest {
    private UserOperation userOperation = new UserOperation();
    private RecordDaoImpl recordDao = new RecordDaoImpl();

    @Test
    public void testAddTradeInvoiceToDB(){
        String dateOfIssue = "2017/11/27";
        assertEquals(userOperation.addTradeInvoiceToDB("user1", "28a/2017",dateOfIssue,"1234567890",
                "Młyn Ambroziak", "żyto", 100.00, 0.00, 0.00,
                23.00, 0.00, 0.00, 123.00 ), "Faktury z taką datą wystawienia: 2017-11-27 nie można rozliczyć! Proszę sprawdzić, czy data jest prawidłowa");//
        dateOfIssue = "2017/12/27";
        assertEquals(userOperation.addTradeInvoiceToDB("user1", "Fv11b/2017",dateOfIssue,"1234567890",
                "Dymek", "rzepak", 100.00, 0.00, 0.00,
               23.00, 0.00, 0.00, 123.00 ), "Taka faktura sprzedaży istnieje już w systemie! " +
               "Istnieje jedynie możliwość jej edycji lub usunięcia.");
    }

    @Test
    public void testGetTradeInvoices(){
        assertNotEquals(userOperation.getTradeInvoices("user1", "12/2017").size(), 0);
        assertEquals(userOperation.getTradeInvoices("user1", "11/2017").size(), 0);
    }

    @Test
    public void testAddPurchaseInvoiceToDB(){
        String dateOfIssue = "2018/01/05";
        assertEquals("Taka faktura istnieje już w bazie! Nie można dodać jej ponownie.", userOperation.addPurchaseInvoiceToDB("user1", "Fv123a/2018",
                dateOfIssue, "1234567890", "Stacja paliw Orlen",
                "olej napędowy", 1000, 0, 0, 230, 0,
                0, 1230,true, 0,0,0));

        dateOfIssue = "2017/12/28";
        assertEquals("Taka faktura istnieje już w bazie! Nie można dodać jej ponownie.",userOperation.addPurchaseInvoiceToDB("user2", "Fv1111/2017",
                dateOfIssue, "1234567899", "Stacja paliw Orlen",
                "olej napędowy", 1000, 0, 0, 230, 0,
                0, 1230,true, 0,0,0) );


    }
}

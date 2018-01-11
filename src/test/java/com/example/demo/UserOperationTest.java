//package com.example.demo;
//
//import com.example.demo.invoices.model.TradeInvoice;
//import com.example.demo.reckoning.dao.RecordDaoImpl;
//import com.example.demo.reckoning.model.TradeRecord;
//import com.example.demo.user.operation.model.UserOperation;
//import org.junit.Test;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import static org.junit.Assert.*;
//
//public class UserOperationTest {
//    private UserOperation userOperation = new UserOperation();
//    private RecordDaoImpl recordDao = new RecordDaoImpl();
//
//    @Test
//    public void testAddTradeInvoiceToDB(){
//        String dateOfIssue = "2017/11/27";
//        assertEquals(userOperation.addTradeInvoiceToDB("user1", "28a/2017",dateOfIssue,"1234567890",
//                "Młyn Ambroziak", "żyto", 100.00, 0.00, 0.00,
//                23.00, 0.00, 0.00, 123.00 ), "Faktury z taką datą wystawienia: 2017-11-27 nie można rozliczyć! Proszę sprawdzić, czy data jest prawidłowa");//
//        dateOfIssue = "2017/12/27";
//        assertEquals(userOperation.addTradeInvoiceToDB("user1", "Fv11b/2017",dateOfIssue,"1234567890",
//                "Dymek", "rzepak", 100.00, 0.00, 0.00,
//               23.00, 0.00, 0.00, 123.00 ), "Taka faktura sprzedaży istnieje już w systemie! " +
//               "Istnieje jedynie możliwość jej edycji lub usunięcia.");
//    }
//
//    @Test
//    public void testGetTradeInvoices(){
//        assertNotEquals(userOperation.getTradeInvoices("user1", "12/2017").size(), 0);
//        assertEquals(userOperation.getTradeInvoices("user1", "11/2017").size(), 0);
//    }
//
//    @Test
//    public void testAddPurchaseInvoiceToDB(){
//        String dateOfIssue = "2018/01/05";
//        assertEquals("Taka faktura istnieje już w bazie! Nie można dodać jej ponownie.", userOperation.addPurchaseInvoiceToDB("user1", "Fv123a/2018",
//                dateOfIssue, "1234567890", "Stacja paliw Orlen",
//                "olej napędowy", 1000, 0, 0, 230, 0,
//                0, 1230,true, 0,0,0));
//
//        dateOfIssue = "2017/12/28";
//        assertEquals("Taka faktura istnieje już w bazie! Nie można dodać jej ponownie.",userOperation.addPurchaseInvoiceToDB("user2", "Fv1111/2017",
//                dateOfIssue, "1234567899", "Stacja paliw Orlen",
//                "olej napędowy", 1000, 0, 0, 230, 0,
//                0, 1230,true, 0,0,0) );
//
//
//    }
//
//    @Test
//    public void testDeleteTradeInvoice(){
//        assertEquals("Usunięcie nie jest możlwie. Taka faktura nie istnieje w bazie lub należy do innego użytkownika.", userOperation.deleteTradeInvoiceFromDB("68/2017", "user2", "11"));
//        assertEquals("Usunięcie nie jest możlwie. Taka faktura nie istnieje w bazie lub należy do innego użytkownika.", userOperation.deleteTradeInvoiceFromDB("22a/2017", "user1", "12"));
//
//    }
//
//    //@Test
//    public void testValidateDeclarationData(){
//        assertEquals("VALID", userOperation.validateDeclarationData(100, "80", "10", "0", "70"));
//        assertEquals("Nadwyżka podaktu należnego nad naliczonym. Brak możliwości otrzymania zwrotu podatku.", userOperation.validateDeclarationData(0, "80", "10", "0", "70"));
//        assertEquals("Suma do zwrotu na rachunek bankowy nie może przekraczać nadwyżki podatku = 40", userOperation.validateDeclarationData(40, "80", "10", "0", "70"));
//        assertEquals("Suma zwrotów w terminach: 25, 60, 180 dni, podana jako: 110 musi być zgodna  z całkowitą sumą zwrotu = 80",  userOperation.validateDeclarationData(100, "80", "10", "0", "100"));
//    }
//
//    //@Test
//    public void testIsOldReckoning(){
//        assertFalse(userOperation.isOldReckoning("1/2018"));
//        assertFalse(userOperation.isOldReckoning("12/2017"));
//        assertTrue(userOperation.isOldReckoning("11/2017"));
//    }
//
//    //@Test
//    public void testIsReckoningFromTheFuture(){
//        assertFalse(userOperation.isReckoningFromTheFuture("1/2018"));
//        assertFalse(userOperation.isReckoningFromTheFuture("11/2010"));
//        assertTrue(userOperation.isReckoningFromTheFuture("3/2018"));
//    }
//}

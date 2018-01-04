package com.example.demo;

import com.example.demo.user.operation.model.InvoiceValidator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvoiceValidatorTest {

    private InvoiceValidator validator = new InvoiceValidator();

    @Test
    public void testIsTradeInvoiceInDB(){
        //Todo error przy serializacji chyba przez LocalDateTime (od timestamp)
        assertTrue(validator.isTradeInvoiceInDB("11a/2017", "user1"));
        assertFalse(validator.isTradeInvoiceInDB("blalblabla", "user1"));
    }
}

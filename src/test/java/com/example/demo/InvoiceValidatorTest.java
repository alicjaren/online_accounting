package com.example.demo;

import com.example.demo.user.operation.model.InvoiceValidator;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvoiceValidatorTest {

    private InvoiceValidator validator = new InvoiceValidator();
    private java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();

    @Test
    public void testIsTradeInvoiceInDB(){
        assertTrue(validator.isTradeInvoiceInDB("11a/2017", "user1"));
        assertFalse(validator.isTradeInvoiceInDB("blalblabla", "user1"));
    }

    @Test
    public void testIsValidDateOfTradeInvoice(){
        Date date = new Date();
        logger.info("\n1test:");
        logger.info("Date now: " + date.toString());
        Date dateOfIssue = new Calendar.Builder().setDate(2017, 11, 27).build().getTime();
        LocalDate localDateTimeOfIssue = dateOfIssue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertTrue(validator.isValidDateOfTradeInvoice(localDateTimeOfIssue));

        logger.info("\n1test:");
        dateOfIssue = new Calendar.Builder().setDate(2018, 0, 5).build().getTime();
        localDateTimeOfIssue = dateOfIssue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        logger.info("Date of issue: " + localDateTimeOfIssue);
        assertTrue(validator.isValidDateOfTradeInvoice(localDateTimeOfIssue));

        logger.info("\n1test:");
        dateOfIssue = new Calendar.Builder().setDate(2018, 1, 12).build().getTime();
        localDateTimeOfIssue = dateOfIssue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertFalse(validator.isValidDateOfTradeInvoice(localDateTimeOfIssue)); //from the future

        logger.info("\n1test:");
        dateOfIssue = new Calendar.Builder().setDate(2016, 11, 27).build().getTime();
        localDateTimeOfIssue = dateOfIssue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertFalse(validator.isValidDateOfTradeInvoice(localDateTimeOfIssue)); //too old

    }

    @Test
    public void testIsValidValueOfVAT(){
        assertFalse(validator.isValidValuesOfVAT(0, 10));
        assertFalse(validator.isValidValuesOfVAT(10, 0));
        assertTrue(validator.isValidValuesOfVAT(0,0));
        assertTrue(validator.isValidValuesOfVAT(10, 10));
    }
}

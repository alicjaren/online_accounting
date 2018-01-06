package com.example.demo.invoices.model;

import java.util.Comparator;

public class TradeInvoiceComparator implements Comparator<TradeInvoice> {

    @Override
    public int compare(TradeInvoice invoice1, TradeInvoice invoice2) {
        return invoice1.getDateOfIssue().compareTo(invoice2.getDateOfIssue());
    }
}

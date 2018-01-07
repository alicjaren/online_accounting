package com.example.demo.invoices.model;

import java.util.Comparator;

public class PurchaseInvoiceComparator implements Comparator<PurchaseInvoice> {
    @Override
    public int compare(PurchaseInvoice purchaseInvoice, PurchaseInvoice t1) {
        return purchaseInvoice.getDateOfIssue().compareTo(t1.getDateOfIssue());
    }
}

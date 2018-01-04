package com.example.demo.invoices.dao;

import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;

public interface InvoiceDao {

    boolean addTradeInvoice(TradeInvoice tradeInvoice);

    boolean addPurchaseInvoice(PurchaseInvoice purchaseInvoice);

    boolean isPurchaseInvoiceInDB(String invoiceNumber, long partnerNIP);

    boolean isTradeInvoiceInDB(String invoiceNumber, String username);
}

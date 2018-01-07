package com.example.demo.invoices.dao;

import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;

import java.util.ArrayList;
import java.util.List;


public interface InvoiceDao {

    boolean addTradeInvoice(TradeInvoice tradeInvoice);

    boolean addPurchaseInvoice(PurchaseInvoice purchaseInvoice);

    boolean isPurchaseInvoiceInDB(String invoiceNumber, long partnerNIP);

    boolean isTradeInvoiceInDB(String invoiceNumber, String username);

    List<TradeInvoice> getTradeInvoices(String username, String tradeRecordName);

    List<PurchaseInvoice> getPurchaseInvoices(String username, String purchaseRecordName);
}

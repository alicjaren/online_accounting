package com.example.demo.invoices.dao;

import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.service.AddingToDB;

public class InvoiceDaoImpl implements InvoiceDao {

    private AddingToDB addingToDB = new AddingToDB();

    @Override
    public boolean addTradeInvoice(TradeInvoice tradeInvoice) {
        return addingToDB.addToDB(tradeInvoice);
    }

    @Override
    public boolean addPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        return addingToDB.addToDB(purchaseInvoice);
    }
}

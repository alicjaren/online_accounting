package com.example.demo.invoices.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.service.AddingToDB;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDaoImpl implements InvoiceDao {

    private AddingToDB addingToDB = new AddingToDB();
    private UserDaoImpl userDao = new UserDaoImpl();
    //private MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();

    @Override
    public boolean addTradeInvoice(TradeInvoice tradeInvoice) {
        return addingToDB.addToDB(tradeInvoice);
    }

    @Override
    public boolean addPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        return addingToDB.addToDB(purchaseInvoice);
    }

    @Override
    public boolean isPurchaseInvoiceInDB(String invoiceNumber, long writingOutNIP) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<TradeInvoice> invoices = new ArrayList<>();
        try {
            invoices = session.createQuery("from PurchaseInvoice p where p.invoiceNumber=? and p.tradePartnerNIP=?")
                        .setParameter(0, invoiceNumber).setParameter(1,writingOutNIP).list();
            session.close();
            return invoices.size() != 0;
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return false;
        }
    }

    @Override
    public boolean isTradeInvoiceInDB(String invoiceNumber, String username){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<TradeInvoice> invoices = new ArrayList<>();
        User user = userDao.getUser(username);
        try {
            invoices = session.createQuery("select i from  MonthlyReckoning m join m.tradeRecord t join t.tradeInvoices i" +
                        " where i.invoiceNumber=? and m.user=?")
                        .setParameter(0, invoiceNumber).setParameter(1,user).list();
            session.close();
            return invoices.size() != 0;
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return false;
        }
    }
}


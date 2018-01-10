package com.example.demo.invoices.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.service.DBOperations;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InvoiceDaoImpl implements InvoiceDao {

    private DBOperations dbOperation = new DBOperations();
    private UserDaoImpl userDao = new UserDaoImpl();
    private Logger logger = Logger.getAnonymousLogger();

    @Override
    public boolean addTradeInvoice(TradeInvoice tradeInvoice) {
        return dbOperation.addToDB(tradeInvoice);
    }

    @Override
    public boolean addPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
        return dbOperation.addToDB(purchaseInvoice);
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

    @Override
    public List<TradeInvoice> getTradeInvoices(String username, String tradeRecordName) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<TradeInvoice> invoices = new ArrayList<>();
        User user = userDao.getUser(username);
        try {
            invoices = session.createQuery("select i from  MonthlyReckoning m join m.tradeRecord t join t.tradeInvoices i" +
                    " where t.name=? and m.user=?")
                    .setParameter(0, tradeRecordName).setParameter(1,user).list();
            session.close();
            return invoices;
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return null;
        }
    }

    @Override
    public List<PurchaseInvoice> getPurchaseInvoices(String username, String purchaseRecordName) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<PurchaseInvoice> invoices;
        User user = userDao.getUser(username);
        try {
            invoices = session.createQuery("select i from  MonthlyReckoning m join m.purchaseRecord p join p.purchaseInvoices i" +
                    " where p.name=? and m.user=?")
                    .setParameter(0, purchaseRecordName).setParameter(1,user).list();
            session.close();
            return invoices;
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return null;
        }
    }

    @Override
    public boolean deleteTradeInvoice(String IdTradeInvoice) {

        TradeInvoice tradeInvoice = new TradeInvoice();
        tradeInvoice.setIdTradeInvoice(Integer.parseInt(IdTradeInvoice));

        return dbOperation.deleteFromDB(tradeInvoice);

    }

    @Override
    public boolean deletePurchaseInvoice(String invoiceId) {

        PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
        purchaseInvoice.setIdPurchaseInvoice(Integer.valueOf(invoiceId));

        return dbOperation.deleteFromDB(purchaseInvoice);
    }


}


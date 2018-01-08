package com.example.demo.service;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.invoices.model.TradeInvoice;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import java.util.logging.Logger;

public class AddingToDB {

    Logger logger = Logger.getLogger("");

    public boolean addToDB(Object o){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            session.save(o);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            session.close();
            return false;
        }
    }

    public boolean deleteFromDB(Object o){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();

        try{
            session.delete(o);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch(GenericJDBCException e) {
            System.out.println("Connection with DB error");
            session.close();
            return false;
        }
    }
}

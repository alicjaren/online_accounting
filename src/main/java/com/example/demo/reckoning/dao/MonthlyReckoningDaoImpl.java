package com.example.demo.reckoning.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.reckoning.model.MonthlyReckoning;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import java.util.List;
import java.util.logging.Logger;

public class MonthlyReckoningDaoImpl implements MonthlyReckoningDao{

    Logger logger = Logger.getLogger("");

    @Override
    public boolean isMonthlyReckoningInDBByName(String name){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<Object> reckonings;
        try {
            reckonings = session.createQuery("from MonthlyReckoning where name=?")
                        .setParameter(0, name).list();
            session.close();
            return reckonings.size() != 0;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }

    @Override
    public boolean addMonthlyReckoning(MonthlyReckoning monthlyReckoning) {

        if (isMonthlyReckoningInDBByName(monthlyReckoning.getName())){
            return false;
        }

        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            session.save(monthlyReckoning);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }

}

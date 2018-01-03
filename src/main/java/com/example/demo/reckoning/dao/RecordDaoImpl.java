package com.example.demo.reckoning.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RecordDaoImpl implements RecordDao {

    Logger logger = Logger.getLogger("");


    @Override
    public boolean isRecordInDB(int id, boolean tradeRecord){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<Object> records;
        try {
            if(tradeRecord){
                records = session.createQuery("from TradeRecord where idTradeRecord=?")
                        .setParameter(0, id).list();
            }
            else{
                records = session.createQuery("from PurchaseRecord where idPurchaseRecord=?")
                        .setParameter(0, id).list();
            }
            session.close();
            return records.size() != 0;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }

    @Override
    public boolean isRecordInDBByName(String name, boolean tradeRecord){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<Object> records;
        try {
            if(tradeRecord){
                records = session.createQuery("from TradeRecord where name=?")
                        .setParameter(0, name).list();
            }
            else{
                records = session.createQuery("from PurchaseRecord where name=?")
                        .setParameter(0, name).list();
            }
            session.close();
            return records.size() != 0;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }


    @Override
    public boolean addTradeRecord(TradeRecord tradeRecord) {
        if(isRecordInDBByName(tradeRecord.getName(), true)){
            return false;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            session.save(tradeRecord);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }

    @Override
    public boolean addPurchaseRecord(PurchaseRecord purchaseRecord) {
        if(isRecordInDBByName(purchaseRecord.getName(), false)){
            return false;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            session.save(purchaseRecord);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }


    @Override
    public TradeRecord getTradeRecord(String name){
        if(!isRecordInDBByName(name, true)){
            return null;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<TradeRecord> records = new ArrayList<>();

        try{
            records = session.createQuery("from TradeRecord where name=?")
                    .setParameter(0, name).list();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return null;
        }
        session.close();
        return records.get(0);
    }

    @Override
    public PurchaseRecord getPurchaseRecord(String name) {
        if(!isRecordInDBByName(name, false)){
            return null;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<PurchaseRecord> records = new ArrayList<>();

        try{
            records = session.createQuery("from PurchaseRecord where name=?")
                    .setParameter(0, name).list();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return null;
        }
        session.close();
        return records.get(0);
    }
}

package com.example.demo.reckoning.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.service.DBOperations;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;

import java.util.List;
import java.util.logging.Logger;

public class MonthlyReckoningDaoImpl implements MonthlyReckoningDao{

    Logger logger = Logger.getLogger("");
    DBOperations DBOperations = new DBOperations();


    @Override
    public boolean isMonthlyReckoningInDBByName(String reckoningName, String userName){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<Object> reckonings;
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.getUser(userName);
        try {
            reckonings = session.createQuery("from MonthlyReckoning m where m.name=? and m.user=?")
                        .setString(0, reckoningName).setParameter(1, user).list();
            session.close();
            return reckonings.size() != 0;
        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
    }

    @Override
    public boolean addMonthlyReckoning(MonthlyReckoning monthlyReckoning) {

        if (isMonthlyReckoningInDBByName(monthlyReckoning.getName(), monthlyReckoning.getUser().getUsername())){
            return false;
        }
        return DBOperations.addToDB(monthlyReckoning);

        /*
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
        }*/
    }

    @Override
    public MonthlyReckoning getMonthlyReckoning(String userName, String reckoningName){
        logger.info("MonthlyReckoning for: userName: " + userName + " reckoningName: " + reckoningName);
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<MonthlyReckoning> reckonings;
        UserDaoImpl userDao = new UserDaoImpl();
        User user = userDao.getUser(userName);
        try {
            reckonings = session.createQuery("from MonthlyReckoning m where m.name=? and m.user=?")
                    .setString(0, reckoningName).setParameter(1, user).list();
            session.close();
            if (reckonings.size() != 0){
                return reckonings.get(0);
            }
            else{
                logger.info("MonthlyReckoning: " + reckoningName + " doesn't exist in DB");
                return null;
            }

        }catch (GenericJDBCException e){
            logger.info("Connection with DB error");
            return null;
        }

    }

}

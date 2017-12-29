package com.example.demo.users.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.users.model.User;
import com.example.demo.users.model.UserRole;
import org.hibernate.*;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.Reference;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public User findByUserName(String username) {

        List<User> users = new ArrayList<User>();

        users = sessionFactory.getCurrentSession()
                .createQuery("from User where username=?")
                .setParameter(0, username)
                .list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public boolean newUserRegistration(User user, UserRole userRole){

        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            session.save(user);
            session.save(userRole);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch (GenericJDBCException e){
            //session.getTransaction().rollback();
            System.out.println("Connection with DB error");
            return false;
        }
    }

    public boolean isUserInDB(String userName){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<User> users = new ArrayList<User>();
        try {
            users = session.createQuery("from User where username=?")
                    .setParameter(0, userName).list();
            session.close();
            //System.out.println("info o uzytkowniku " + userName + ": " + users);
            return users.size() != 0;
        }catch (GenericJDBCException e){
            //session.getTransaction().rollback();
            System.out.println("Connection with DB error");
            return false;
        }
    }

}
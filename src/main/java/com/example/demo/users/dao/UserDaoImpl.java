package com.example.demo.users.dao;

import java.util.*;
import java.util.logging.Logger;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.config.SpringSecurityConfig;
import com.example.demo.service.DBOperations;
import com.example.demo.users.model.User;
import com.example.demo.users.model.UserRole;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;
    private Logger logger = Logger.getAnonymousLogger();
    private DBOperations DBOperations = new DBOperations();

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
        return DBOperations.addToDB(user) && DBOperations.addToDB(userRole);
    }

    public boolean isUserInDB(String userName){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<User> users = new ArrayList<User>();
        try {
            users = session.createQuery("from User where username=?")
                    .setParameter(0, userName).list();
            //session.close();
            return users.size() != 0;
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return false;
        }
        finally{
            session.close();
        }
    }

    @Override
    public boolean deleteUser(String userName) {
        if (!isUserInDB(userName)){
            logger.info("Such user: " + userName + " doesn't exist");
            return false;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            User user = new User();
            user.setUsername(userName);
            try {
                logger.info("user to delete: " + userName);
                session.delete(user);
                Transaction tx = session.beginTransaction();
                tx.commit();
                //session.close();
            }catch (ConstraintViolationException cve){
                logger.info("This user can't be deleted - constraint key exists");
                return false;
            }
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return false;
        }
        finally{
            session.close();
        }
        return true;
    }


    @Override
    public Set<UserRole> getUserRoles(String userName) {
        if(!isUserInDB(userName)){
            logger.info("Such user: " + userName + " doesn't exist");
            return null;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<UserRole> userRoles = new ArrayList<>();

        try{
            userRoles = session.createQuery("from UserRole where username=?")
                    .setParameter(0, userName).list();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return null;
        }
        finally{
            session.close();
        }
        return new HashSet<>(userRoles);
    }


    @Override
    public boolean deleteUserRoles(String userName) {
        if(!isUserInDB(userName)){
            logger.info("Such user: " + userName + " doesn't exist in DB");
            return false;
        }

        Set<UserRole> userRoles = getUserRoles(userName);

        if (userRoles.isEmpty()){
            logger.info("User hasn't any roles");
            return true;
        }

        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();

        try{
            userRoles.forEach((ur) -> session.delete(ur));
            Transaction tx = session.beginTransaction();
            tx.commit();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
        finally{
            session.close();
        }
        return true;
    }


    @Override
    public User getUser(String userName) {
        if(!isUserInDB(userName)){
            return null;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<User> users = new ArrayList<>();

        try{
            users = session.createQuery("from User where username=?")
                    .setParameter(0, userName).list();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return null;
        }
        finally{
            session.close();
        }
        return users.get(0);
    }

    @Override
    public User getUserByNIP(long NIP){
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<User> users = new ArrayList<>();

        try{
            users = session.createQuery("from User join Person p where p.NIP=?")
                    .setParameter(0, NIP).list();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return null;
        }
        finally{
            session.close();
        }
        if (users.isEmpty()){
            logger.info("User with NIP: " + NIP + " doesn't exists!");
            return null;
        }
        else{
            return users.get(0);
        }
    }

    @Override
    public boolean isCorrectPassword(String username, String password) {
        if(!isUserInDB(username)){
            return false;
        }
        SpringSecurityConfig security = new SpringSecurityConfig();
        PasswordEncoder passwordEncoder = security.passwordEncoder();
        //String hashPassword = passwordEncoder.encode(password);
        User user = getUser(username);

        return ((!(user == null)) && passwordEncoder.matches(password, user.getPassword()));
    }


    @Override
    public boolean changePassword(String username, String newPassword) {
        if(!isUserInDB(username)){
            logger.info("Such user: " + username  + " doesn't exist in DB");
            return false;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();

        SpringSecurityConfig security = new SpringSecurityConfig();
        PasswordEncoder passwordEncoder = security.passwordEncoder();
        String hashPassword = passwordEncoder.encode(newPassword);

        User updatedUser = new User(username, hashPassword, true);

        try{
            session.update(updatedUser);
            Transaction tx = session.beginTransaction();
            tx.commit();
        }catch(GenericJDBCException e){
            logger.info("Connection with DB error");
            return false;
        }
        finally {
            session.close();
        }
        return true;
    }
}
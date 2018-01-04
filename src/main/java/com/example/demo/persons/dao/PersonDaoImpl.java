package com.example.demo.persons.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.persons.model.Person;
import com.example.demo.service.AddingToDB;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PersonDaoImpl implements PersonDao {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private AddingToDB addingToDB = new AddingToDB();

    @Override
    public Person findByUserName(String username) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<Person> person = new ArrayList<>();
        try {
            person = session.createQuery("from Person where username=?")
                    .setParameter(0, username).list();
            session.close();
            LOGGER.info("info o osobie " + username + ": " + person.toString());
            if (person.size() > 0) {
                return person.get(0);
            }
            else{
                return null;
            }
        }catch (GenericJDBCException e){
            //session.getTransaction().rollback();
            LOGGER.info("Connection with DB error");
            return null;
        }
    }

    @Override
    public boolean addNewPerson(Person person) {
        return addingToDB.addToDB(person);
        /*DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            session.save(person);
            Transaction tx = session.beginTransaction();
            tx.commit();
            session.close();
            return true;
        }catch (GenericJDBCException e){
            //session.getTransaction().rollback();
            LOGGER.info("Connection with DB error");
            return false;
        }*/
    }

    @Override
    public List<Person> getPersonsList() {
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        List<Person> persons;
        try {
            persons = session.createQuery("from Person").list();
            session.close();
            return persons;
        }catch (GenericJDBCException e){
            //session.getTransaction().rollback();
            LOGGER.info("Connection with DB error");
            return null;
        }
    }

    @Override
    public boolean deletePerson(String username) {
        UserDaoImpl userDao = new UserDaoImpl();
        if(!userDao.isUserInDB(username)){
            return false;
        }
        DatabaseConfig dbConfig = new DatabaseConfig();
        SessionFactory factory = dbConfig.sessionFactory();
        Session session = factory.openSession();
        try {
            Person person = new Person();
            person.setUsername(username);
            try {
                LOGGER.info("person to delete: " + username);
                session.delete(person);
                Transaction tx = session.beginTransaction();
                tx.commit();
                session.close();
            }catch (ConstraintViolationException cve){
                LOGGER.info("This person can't be deleted - constraint key exists");
                return false;
            }
        }catch (GenericJDBCException e){
            System.out.println("Connection with DB error");
            return false;
        }
        return true;
    }
}

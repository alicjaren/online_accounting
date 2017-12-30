package com.example.demo.persons.dao;

import com.example.demo.config.DatabaseConfig;
import com.example.demo.persons.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

public class PersonDaoImpl implements PersonDao {

    @Override
    public Person findByUserName(String username) {
        return null;
    }

    @Override
    public boolean addNewPerson(Person person) {
        DatabaseConfig dbConfig = new DatabaseConfig();
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
            System.out.println("Connection with DB error");
            return false;
        }
    }
}

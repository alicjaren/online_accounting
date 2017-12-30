package com.example.demo.persons.dao;

import com.example.demo.persons.model.Person;
import java.util.List;


public interface PersonDao {
    Person findByUserName(String username);
    boolean addNewPerson(Person person);
    List<Person> getPersonsList();
    boolean deletePerson(String username);
}

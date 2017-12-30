package com.example.demo.persons.dao;

import com.example.demo.persons.model.Person;

public interface PersonDao {
    Person findByUserName(String username);
    boolean addNewPerson(Person person);
}

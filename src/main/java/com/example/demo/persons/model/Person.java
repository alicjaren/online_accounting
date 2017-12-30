package com.example.demo.persons.model;

import com.example.demo.users.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "persons", catalog = "test")
public class Person {
    private String username;
    private String name;
    private String surname;
    private String address;
    private String email;
    private long NIP;
    private long phoneNumber;
    private Date dateOfBirth;
    private String nameOfRevenue;
    private User user;


    public Person(){

    }

    public Person(String username, String name, String surname, String address, String email, long NIP,
                  long phoneNumber, Date dateOfBirth, String nameOfRevenue, User user) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.NIP = NIP;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.nameOfRevenue = nameOfRevenue;
        this.user = user;
    }

    @Id
    @Column(name = "username", unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "surname", nullable = false, length = 60)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(name = "address", nullable = false, length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name="email", nullable = false, length = 45)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "NIP", nullable = false, length = 10)
    public long getNIP() {
        return NIP;
    }

    public void setNIP(long NIP) {
        this.NIP = NIP;
    }

    @Column(name = "phone_number", nullable = false, length = 9)
    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name="date_of_birth", nullable = false)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "name_of_revenue", nullable = false, length = 70)
    public String getNameOfRevenue() {
        return nameOfRevenue;
    }

    public void setNameOfRevenue(String nameOfRevenue) {
        this.nameOfRevenue = nameOfRevenue;
    }
}

package com.example.demo.users.model;

import com.example.demo.persons.model.Person;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "users", catalog = "test")
public class User {
    private String username;
    private String password;
    private boolean enabled;
    private Set<UserRole> userRole = new HashSet<UserRole>(0);
    private Person person;

    public User() {
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String username, String password,
                boolean enabled, Set<UserRole> userRole) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.userRole = userRole;
    }


    public User(String username, String password, boolean enabled, Person person) {

        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.person = person;
    }

    @Id
    @Column(name = "username", unique = true, nullable = false, length = 45)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


    @Column(name = "password", nullable = false, length = 60)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "enabled", nullable = false)
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserRole> getUserRole() {
        return this.userRole;
    }

    public void setUserRole(Set<UserRole> userRole) {
        this.userRole = userRole;
    }
}

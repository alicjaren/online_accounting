package com.example.demo.users.dao;

import com.example.demo.users.model.User;
import com.example.demo.users.model.UserRole;

public interface UserDao {

    User findByUserName(String username);
    boolean newUserRegistration(User user, UserRole userRole);
    boolean isUserInDB(String userName);

}
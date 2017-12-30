package com.example.demo.users.dao;

import com.example.demo.users.model.User;
import com.example.demo.users.model.UserRole;

import java.util.Set;

public interface UserDao {

    User findByUserName(String username);

    boolean newUserRegistration(User user, UserRole userRole);

    boolean isUserInDB(String userName);

    boolean deleteUser(String userName);

    Set<UserRole> getUserRoles(String userName);

    boolean deleteUserRoles(String userName);

    User getUser(String userName);

    boolean isCorrectPassword(String username, String password);

    boolean changePassword(String username, String newPassword);

}
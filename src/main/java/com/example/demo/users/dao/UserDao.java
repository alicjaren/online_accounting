package com.example.demo.users.dao;

import com.example.demo.users.model.User;

public interface UserDao {

    User findByUserName(String username);

}
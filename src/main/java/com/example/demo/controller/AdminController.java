package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @RequestMapping("/admin/users/list")
    public String usersList() {
        return "/admin_user_list";
    }

    @RequestMapping("/admin/users/add")
    public String addUsers() {
        return "/admin_add_user";
    }
}

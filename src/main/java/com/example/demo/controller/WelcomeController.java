package com.example.demo.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String home1() {
        return "/home";
    }

    @RequestMapping("/home")
    public String home() {
        return "/home";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "/admin";
    }

    @RequestMapping("/user")
    public String user(HttpServletRequest request) {
       /* Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated()){
            return "/user";
        }*/
        return "/user";
    }

    @RequestMapping("/about")
    public String about() {
        return "/about";
    }

    @RequestMapping("/login")
    public String login() {
        return "/login";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "/login";
    }

    @RequestMapping("/403")
    public String error403() {
        return "/error/403";
    }

}

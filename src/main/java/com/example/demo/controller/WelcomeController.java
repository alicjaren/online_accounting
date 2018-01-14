package com.example.demo.controller;

import com.example.demo.persons.dao.PersonDaoImpl;
import com.example.demo.persons.model.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class WelcomeController {

    private PersonDaoImpl personDao = new PersonDaoImpl();

    @RequestMapping("/")
    public String home1() {
        return "/home";
    }

    @RequestMapping("/home")
    public String home() {
        return "/home";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminName = auth.getName(); //get logged in username
        Person person = personDao.findByUserName(adminName);
        model.addAttribute("person", person.getName() + " " + person.getSurname());
        return "/admin";
    }

    @RequestMapping("/user")
    public String user(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        Person person = personDao.findByUserName(username);
        model.addAttribute("person", person.getName() + " " + person.getSurname());
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

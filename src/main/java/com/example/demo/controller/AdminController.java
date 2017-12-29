package com.example.demo.controller;

import com.example.demo.admin.operation.model.AdminOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.logging.Logger;

@Controller
public class AdminController {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    @RequestMapping("/admin/users/list")
    public String usersList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        LOGGER.info("rola usera: " + role);
        return "/admin_user_list";
    }

    @RequestMapping("/admin/users/add")
    public String addUsersRenderForm(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        LOGGER.info("rola usera: " + role);
        return "/admin_add_user";
    }

    @RequestMapping(value = "/admin/users/add", method = RequestMethod.POST)
    public String addUser(@RequestParam("username") String login, @RequestParam("password") String password,
                          @RequestParam("password2") String password2, @RequestParam("email") String email,
                          @RequestParam("name") String name, @RequestParam("surname") String surname,
                          @RequestParam("address") String address, @RequestParam("NIP") String NIP,
                          @RequestParam("dateOfBirth") String dateOfBirth, @RequestParam("phoneNr") String phoneNr,
                          @RequestParam("nameOfRevenue") String nameOfRevenue,  Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        LOGGER.info("rola usera: " + role);
        LOGGER.info("Registration for: login: " + login + "\npasswords: " + password + " " + password2
            + " name: " +  " email: " + email + name + " surname: " + surname + " \naddress: "
                + address + " dateOfBirth: " + dateOfBirth + " NIP: " + NIP + " phoneNr: " + phoneNr +
                " \nrevenue: " + nameOfRevenue);

        AdminOperation admin = new AdminOperation();
        String result  = admin.addNewUser(login, password, password2, email, name, surname, address, NIP, dateOfBirth,
                phoneNr, nameOfRevenue);
        if (result.equals("SUCCESS")){
            model.addAttribute("addedUser", "Dodano nowego użytkownika.");
            return "/admin";
        }
        else{
            model.addAttribute("failedAdded", result);
            return "/admin";
        }
    }
}


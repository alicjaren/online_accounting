package com.example.demo.controller;

import com.example.demo.admin.operation.model.AdminOperation;
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
        return "/admin_user_list";
    }

    @RequestMapping("/admin/users/add")
    public String addUsersRenderForm() {
        return "/admin_add_user";
    }

    @RequestMapping(value = "/admin/users/add", method = RequestMethod.POST)
    public String addUser(@RequestParam("username") String login, @RequestParam("password") String password,
                          @RequestParam("password2") String password2, @RequestParam("email") String email,
                          @RequestParam("name") String name, @RequestParam("surname") String surname,
                          @RequestParam("address") String address, @RequestParam("NIP") String NIP,
                          @RequestParam("dateOfBirth") String dateOfBirth, @RequestParam("phoneNr") String phoneNr,
                          @RequestParam("nameOfRevenue") String nameOfRevenue,  Model model) {

        LOGGER.info("Rejestracja dla loginu: " + login + "\npasswords: " + password + " " + password2
            + " name: " +  " email: " + email + name + " surname: " + surname + " \naddress: "
                + address + " dateOfBirth: " + dateOfBirth + " NIP: " + NIP + " phoneNr: " + phoneNr +
                " \nrevenue: " + nameOfRevenue);

        AdminOperation admin = new AdminOperation();
        
        if (admin.addNewUser(login, password, password2, email, name, surname, address, NIP, dateOfBirth,
                phoneNr, nameOfRevenue)){
            model.addAttribute("addedUser", "Dodano nowego użytkownika.");
            return "/admin";
        }

        model.addAttribute("failedAdded", "Błąd przy dodawaniu nowego użytkownika.");
        return "/admin";
    }
}


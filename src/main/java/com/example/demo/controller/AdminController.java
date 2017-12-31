package com.example.demo.controller;

import com.example.demo.admin.operation.model.AdminOperation;
import com.example.demo.persons.model.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class AdminController {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private AdminOperation adminOperation = new AdminOperation();

    @RequestMapping("/admin/users/list")
    public String usersList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        LOGGER.info("rola usera: " + role);
        List<Person> persons = adminOperation.getPersonsList();
        model.addAttribute("persons", persons);
        LOGGER.info(persons.toString());
        return "/admin_user_list";
    }

    @RequestMapping("/admin/users/add")
    public String addUsersRenderForm(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        LOGGER.info("rola usera: " + role);
        return "/admin_add_user";
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.POST)
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

        String result  = adminOperation.addNewUser(login, password, password2, email, name, surname, address, NIP, dateOfBirth,
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

    @RequestMapping(value="/admin/user",  method = RequestMethod.DELETE)
    public String deleteUser(@RequestParam("username") String username, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        LOGGER.info("rola usera: " + role);
        if (adminOperation.deleteUser(username)){
            model.addAttribute("deletedSuccess", "Użytkownik usunięty pomyślnie.");
        }
        else{
            model.addAttribute("deletedError", "Nie można usunąć użytkownika. " +
                    "Proszę najpierw usunąć jego zależności (rozliczenia i faktury)");
        }
        return "/admin_user_list";
    }

    @RequestMapping("/admin/password/changing")
    public String getChangingPasswordForm(Model model){
        model.addAttribute("admin", "true");
        return "/change_password";
    }


}


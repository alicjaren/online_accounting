package com.example.demo.controller;

import com.example.demo.admin.operation.model.AdminOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class UserController {

    Logger LOGGER = Logger.getLogger("");

    @RequestMapping("/user/password/changing")
    public String getChangingPasswordForm(Model model){
        return "/change_password";
    }


    @RequestMapping(value="/user/password/changing", method=RequestMethod.POST)
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("newPassword2") String newPassword2, Model model,
                                 HttpServletRequest request){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        LOGGER.info("Changing password for user: " + name);

        AdminOperation admin = new AdminOperation();
        String result = admin.changePassword(name, currentPassword, newPassword, newPassword2);

        if (request.isUserInRole("ROLE_ADMIN")){
            model.addAttribute("admin", "true");
        }

        if(result.equals("SUCCESS")){
            model.addAttribute("result", "Hasło zmienione pomyślnie.");
            if(request.isUserInRole("ROLE_ADMIN")){
                return "/admin";
            }
            return "/user";
        }
        else{
            model.addAttribute("error", result);
            return "/change_password";
        }
    }

    @RequestMapping("/user/invoice/adding")
    public String getAddingInvoiceForm(){
        return "/add_invoice";
    }

    @RequestMapping(value = "user/invoice/adding", method = RequestMethod.POST)
    public String addInvoice(Model model){
        model.addAttribute("result", "Dodano nową fakturę");
        return "/user";
    }
}

package com.example.demo.controller;

import com.example.demo.admin.operation.model.AdminOperation;
import com.example.demo.user.operation.model.UserOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;

@Controller
public class UserController {

    private Logger logger = Logger.getAnonymousLogger();

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
        logger.info("Changing password for user: " + name);

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

    @RequestMapping("/user/trade/invoice/adding")
    public String getAddingTradeInvoiceForm() {
        return "/add_trade_invoice";
    }

    @RequestMapping(value = "user/trade/invoice/adding", method = RequestMethod.POST)
    public String addTradeInvoice(@RequestParam("invoiceNumber") String invoiceNumber,
                             @RequestParam("dateOfIssue") String dateOfIssue,
                             @RequestParam("tradePartnerNIP") String tradePartnerNIP,
                             @RequestParam("tradePartnerName") String tradePartnerName,
                             @RequestParam("dealingThingName") String dealingThingName,
                             @RequestParam("net23") double net23, @RequestParam("vat23") double vat23,
                             @RequestParam("net8") double net8, @RequestParam("vat8") double vat8,
                             @RequestParam("net5") double net5, @RequestParam("vat5") double vat5,
                             @RequestParam("gross") double gross, Model model){

        logger.info("Adding tradeInvoice: number: " + invoiceNumber + " data: " + dateOfIssue + " partnerNIP: " + tradePartnerNIP +
        " partnerName: " + tradePartnerName + " dealingThing: " + dealingThingName + " gross: " + gross);

        UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username

        String result = userOperation.addTradeInvoiceToDB(username, invoiceNumber, dateOfIssue, tradePartnerNIP,
                tradePartnerName, dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross);

        if (result.equals("SUCCESS")){
            model.addAttribute("result", "Nowa faktura zakupu została dodana do rejestru");
        }
        else{
            model.addAttribute("error", result);
            return "/add_trade_invoice";
        }

        return "/user";
    }

    @RequestMapping("/user/purchase/invoice/adding")
    public String getAddingPurchaseInvoiceForm() {
        return "/add_purchase_invoice";
    }


    @RequestMapping(value = "/user/purchase/invoice/adding", method = RequestMethod.POST)
    public String getAddingPurchaseInvoice(Model model){
        logger.info("Add purchase invoice");
        return "/user";
    }
}

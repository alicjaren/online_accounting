package com.example.demo.controller;

import com.example.demo.admin.operation.model.AdminOperation;
import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.persons.dao.PersonDaoImpl;
import com.example.demo.persons.model.Person;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.user.operation.model.UserOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class UserController {

    private Logger logger = Logger.getAnonymousLogger();
    private UserOperation userOperation = new UserOperation();
    private PersonDaoImpl personDao = new PersonDaoImpl();

    @RequestMapping("/user/password/changing")
    public String getChangingPasswordForm(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        Person person = personDao.findByUserName(username);
        model.addAttribute("person", person.getName() + " " + person.getSurname());
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
        model.addAttribute("person", getPersonNameAndSurname());

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
    public String getAddingTradeInvoiceForm(Model model) {
        model.addAttribute("person", getPersonNameAndSurname());
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

        model.addAttribute("person", getPersonNameAndSurname());
        UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username

        String result = userOperation.addTradeInvoiceToDB(username, invoiceNumber, dateOfIssue, tradePartnerNIP,
                tradePartnerName, dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross);

        if (result.equals("SUCCESS")){
            model.addAttribute("result", "Nowa faktura sprzedaży została dodana do rejestru");
        }
        else{
            model.addAttribute("error", result);
            return "/add_trade_invoice";
        }

        return "/user";
    }

    @RequestMapping("/user/purchase/invoice/adding")
    public String getAddingPurchaseInvoiceForm(Model model) {
        model.addAttribute("person", getPersonNameAndSurname());
        return "/add_purchase_invoice";
    }


    @RequestMapping(value = "/user/purchase/invoice/adding", method = RequestMethod.POST)
    public String addingPurchaseInvoice(@RequestParam("invoiceNumber") String invoiceNumber,
                                           @RequestParam("dateOfIssue") String dateOfIssue,
                                           @RequestParam("tradePartnerNIP") String tradePartnerNIP,
                                           @RequestParam("tradePartnerName") String tradePartnerName,
                                           @RequestParam("dealingThingName") String dealingThingName,
                                           @RequestParam("net23") double net23, @RequestParam("vat23") double vat23,
                                           @RequestParam("net8") double net8, @RequestParam("vat8") double vat8,
                                           @RequestParam("net5") double net5, @RequestParam("vat5") double vat5,
                                           @RequestParam("gross") double gross,
                                           @RequestParam("fixedAssetsNet") double fixedAssetsNet,
                                           @RequestParam("fixedAssetsVat") double fixedAssetsVat,
                                           @RequestParam("fixedAssetsGross") double fixedAssetsGross,
                                           @RequestParam("deducted") String deductedValue, Model model){

        logger.info("Adding purchaseInvoice: number: " + invoiceNumber + " data: " + dateOfIssue + " partnerNIP: " + tradePartnerNIP +
                " partnerName: " + tradePartnerName + " dealingThing: " + dealingThingName + " gross: " + gross);

        boolean deducted = deductedValue.equals("YES");
        UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username

        model.addAttribute("person", getPersonNameAndSurname());
        String result = userOperation.addPurchaseInvoiceToDB(username, invoiceNumber, dateOfIssue, tradePartnerNIP,
                tradePartnerName, dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross, deducted,
                fixedAssetsNet, fixedAssetsVat, fixedAssetsGross);

        if (result.equals("SUCCESS")){
            model.addAttribute("result", "Nowa faktura zakupu została dodana do rejestru");
        }
        else{
            model.addAttribute("error", result);
            return "/add_purchase_invoice";
        }

        return "/user";
    }

    @RequestMapping("/user/trade/invoice/listing")
    public String getListingTradeInvoicesForm(Model model){
        model.addAttribute("registerName", " rejestru faktur sprzedaży:");
        model.addAttribute("url", "/user/trade/invoice/listing");
        model.addAttribute("person", getPersonNameAndSurname());
        return "/get_register";
    }

    @RequestMapping(value = "/user/trade/invoice/listing", method = RequestMethod.POST)
    public String getListingTradeInvoices(@RequestParam("month") String month, @RequestParam("year") String year,
                                          Model model){
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        String tradeRecordName = month + "/" + year;
        //UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("person", getPersonNameAndSurname());

        logger.info("Listing of trade invoices for user: " + username + " for month: " + month + " and year: " + year);

        ArrayList<TradeInvoice> invoices = userOperation.getTradeInvoices(username, tradeRecordName);
        model.addAttribute("invoices", invoices);

        return "/list_trade_invoices";
    }


    @RequestMapping("/user/purchase/invoice/listing")
    public String getListingPurchaseInvoicesForm(Model model){
        model.addAttribute("registerName", " rejestru faktur zakupu:");
        model.addAttribute("url", "/user/purchase/invoice/listing");
        model.addAttribute("person", getPersonNameAndSurname());

        return "/get_register";
    }

    @RequestMapping(value = "/user/purchase/invoice/listing", method = RequestMethod.POST)
    public String getListingPurchaseInvoices(@RequestParam("month") String month, @RequestParam("year") String year,
                                             Model model){
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        String purchaseRecordName = month + "/" + year;
        //UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("person", getPersonNameAndSurname());

        logger.info("Listing of purchase invoices for user: " + username + " for month: " + month + " and year: " + year);

        ArrayList<PurchaseInvoice> invoices = userOperation.getPurchaseInvoices(username, purchaseRecordName);
        model.addAttribute("invoices", invoices);

        return "/list_purchase_invoices";
    }


    @RequestMapping(value="/user/trade/invoice/deleting", method=RequestMethod.POST)
    public String deleteTradeInvoice(@RequestParam("invoiceId") String invoiceId,
                                     @RequestParam("invoiceNumber") String invoiceNumber, Model model){

        //UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        logger.info("Deleting trade invoice number: " + invoiceNumber + " id: " + invoiceId + " by user " + username);
        model.addAttribute("person", getPersonNameAndSurname());

        String result = userOperation.deleteTradeInvoiceFromDB(invoiceNumber, username, invoiceId);
        if(result.equals("SUCCESS")){
            model.addAttribute("result", "Faktura o numerze " + invoiceNumber + " została usunięta");
            return "/user";
        }
        else{
            model.addAttribute("error", result);
            return  "/user";
        }
    }

    @RequestMapping(value="/user/purchase/invoice/deleting", method=RequestMethod.POST)
    public String deletePurchaseInvoice(@RequestParam("invoiceId") String invoiceId,
                                        @RequestParam("invoiceNumber") String invoiceNumber,
                                        @RequestParam("tradePartnerNIP") String tradePartnerNIP,  Model model){

        //UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        logger.info("Deleting purchase invoice number: " + invoiceNumber + " id: " + invoiceId + " by user " + username);
        model.addAttribute("person", getPersonNameAndSurname());

        String result = userOperation.deletePurchaseInvoiceFromDB(invoiceNumber, username, invoiceId, tradePartnerNIP);
        if(result.equals("SUCCESS")){
            model.addAttribute("result", "Faktura o numerze " + invoiceNumber + " została usunięta");
            return "/user";
        }
        else{
            model.addAttribute("error", result);
            return  "/user";
        }
    }

    @RequestMapping("/user/trade/record/listing")
    public String getListingTradeRecordForm(Model model){
        model.addAttribute("registerName", " rejestru sprzedaży");
        model.addAttribute("url", "/user/trade/record/listing");
        model.addAttribute("person", getPersonNameAndSurname());

        return "/get_register";
    }

    @RequestMapping(value = "/user/trade/record/listing", method = RequestMethod.POST)
    public String getListingTradeRecord(@RequestParam("month") String month, @RequestParam("year") String year,
                                             Model model){
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        String tradeRecordName = month + "/" + year;
        //UserOperation userOperation = new UserOperation();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("person", getPersonNameAndSurname());

        logger.info("Listing of trade record for user: " + username + " for month: " + month + " and year: " + year);

        TradeRecord tradeRecord = userOperation.getTradeRecord(username, tradeRecordName);
        model.addAttribute("record", tradeRecord);

        return "/list_trade_record";
    }


    @RequestMapping("/user/purchase/record/listing")
    public String getListingPurchaseRecordForm(Model model){
        model.addAttribute("registerName", " rejestru zakupu");
        model.addAttribute("url", "/user/purchase/record/listing");
        model.addAttribute("person", getPersonNameAndSurname());

        return "/get_register";
    }

    @RequestMapping(value = "/user/purchase/record/listing", method = RequestMethod.POST)
    public String getListingPurchaseRecord(@RequestParam("month") String month, @RequestParam("year") String year,
                                        Model model){
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        String purchaseRecordName = month + "/" + year;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("person", getPersonNameAndSurname());

        logger.info("Listing of purchase record for user: " + username + " for month: " + month + " and year: " + year);

        PurchaseRecord purchaseRecord = userOperation.getPurchaseRecord(username, purchaseRecordName);
        model.addAttribute("record", purchaseRecord);

        return "/list_purchase_record";
    }

    @RequestMapping("/user/reckoning/generating")
    public String getGeneratingMonthlyReckoningForm(Model model){
        model.addAttribute("registerName", " rozliczenia miesiecznego do wygenerowania");
        model.addAttribute("url", "/user/reckoning/generating");
        model.addAttribute("person", getPersonNameAndSurname());

        return "/get_register";
    }

    @RequestMapping(value = "/user/reckoning/generating", method = RequestMethod.POST)
    public String generateMonthlyReckoning(@RequestParam("month") String month,
                                           @RequestParam("year") String year,
                                           Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("person", getPersonNameAndSurname());

        String reckoningName = month + "/" + year;
        logger.info("Generate monthly reckoning: " + reckoningName + " for user " + username);


        if(userOperation.isOldReckoning(reckoningName)){
            model.addAttribute("error", "Taka deklaracja powinna już zostać złożona w Urzędzie Skarobowym." +
                    " Czy chcesz ją wygenerować ponownie?");
        }

        if(userOperation.isReckoningFromTheFuture(reckoningName)){
            model.addAttribute("error", "Wybrane rozliczenie dotyczy przyszłości, nie ma w nim żadnych faktur.");
        }

        Map<String, Integer> preDeclaration = userOperation.generateMonthlyReckoning(username, reckoningName);

        if(preDeclaration != null){

            model.addAttribute("month", month);
            model.addAttribute("year", year);
            model.addAttribute("map", preDeclaration);

            if(preDeclaration.get("overhang") == 0){ //tax for revenue, final reckoning is updated
                logger.info("Final declaration is updating, overhang = 0.");
                return "/declaration";
            }

            else{
                logger.info("PreDeclaration is generated, sum of refunds is required");
                return "/get_final_declaration";
            }
        }

        else{
            model.addAttribute("error", "Przepraszamy, wystąpiły błędy techniczne. Prosimy spóbować ponownie.");
            return "/user/reckoning/generating";
        }
    }

    @RequestMapping(value = "/user/declaration", method = RequestMethod.POST)
    public String generateFinalDeclaration(@RequestParam("month") String month,
                                           @RequestParam("year") String year,
                                           @RequestParam("sumForClient") String sumForClient,
                                           @RequestParam("in25days") String in25days,
                                           @RequestParam("in60days") String in60days,
                                           @RequestParam("in180days") String in180days,
                                           Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("person", getPersonNameAndSurname());

        logger.info("Generating final declaration for user: " + username + " month: " + month +  " year: " + year +
            " forClient: " + sumForClient + " in25: " + in25days + " in60: " + in60days + " in180: " + in180days);

        String reckoningName = month + "/" + year;
        Map<String, Integer> preDeclaration = userOperation.generateMonthlyReckoning(username, reckoningName);

        model.addAttribute("year", year);
        model.addAttribute("month", month);


        String result = userOperation.validateDeclarationData(preDeclaration.get("overhang"), sumForClient, in25days, in60days, in180days);
        if(!result.equals("VALID")){
            model.addAttribute("map", preDeclaration);
            model.addAttribute("error", result);
            return  "/get_final_declaration";
        }

        Map<String, Integer> finalDeclaration = userOperation.updateMonthlyReckoning(preDeclaration, sumForClient,
                in25days, in60days, in180days, reckoningName, username);

        if (finalDeclaration == null){
            logger.info("Error by updating MonthlyReckoning");
            model.addAttribute("map", preDeclaration);
            model.addAttribute("error", "Przepraszamy, wystąpiły błędy techniczne. Prosimy spróbować ponownie.");
            return  "/get_final_declaration";
        }

        model.addAttribute("map", finalDeclaration);

        return "/declaration";
    }

    @RequestMapping("/user/reckoning/listing")
    public String getMonthlyReckoningForm(Model model){
        model.addAttribute("registerName", " rozliczenia miesiecznego do podglądu");
        model.addAttribute("url", "/user/reckoning/listing");
        model.addAttribute("person", getPersonNameAndSurname());

        return "/get_register";
    }

    @RequestMapping(value = "/user/reckoning/listing", method = RequestMethod.POST)
    public String getMonthlyReckoning(@RequestParam("month") String month,
                                      @RequestParam("year") String year,  Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        String reckoningName = month + "/" + year;
        model.addAttribute("person", getPersonNameAndSurname());

        logger.info("Get monthly reckoning: " + reckoningName + " for user " + username);

        model.addAttribute("month", month);
        model.addAttribute("year", year);

        model.addAttribute("reckoning", userOperation.getMonthlyReckoning(username, reckoningName));
        return "/list_monthly_reckoning";
    }

    private String getPersonNameAndSurname(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        Person person = personDao.findByUserName(username);
        return person.getName() + " " + person.getSurname();
    }
}

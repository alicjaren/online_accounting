package com.example.demo.user.operation.model;

import com.example.demo.admin.operation.model.Validator;
import com.example.demo.invoices.dao.InvoiceDaoImpl;
import com.example.demo.invoices.model.PurchaseInvoice;
import com.example.demo.invoices.model.PurchaseInvoiceComparator;
import com.example.demo.invoices.model.TradeInvoice;
import com.example.demo.invoices.model.TradeInvoiceComparator;
import com.example.demo.reckoning.dao.MonthlyReckoningDaoImpl;
import com.example.demo.reckoning.dao.RecordDaoImpl;
import com.example.demo.reckoning.model.MonthlyReckoning;
import com.example.demo.reckoning.model.PurchaseRecord;
import com.example.demo.reckoning.model.TradeRecord;
import com.example.demo.reckoning.service.ReckoningGenerator;
import com.example.demo.service.DBOperations;
import com.example.demo.users.dao.UserDao;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.text.TabableView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/*adding, updating, selecting and deleting invoices*/
public class UserOperation {

    private Logger logger = Logger.getAnonymousLogger();
    private InvoiceValidator validator = new InvoiceValidator();
    private RecordDaoImpl recordDao = new RecordDaoImpl();
    private InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    private ReckoningGenerator reckoningGenerator = new ReckoningGenerator();
    private DeclarationValidator declarationValidator = new DeclarationValidator();
    private MonthlyReckoningDaoImpl monthlyReckoningDao = new MonthlyReckoningDaoImpl();

    public String addTradeInvoiceToDB(String username, String invoiceNumber, String dateOfIssue, String tradePartnerNIP,
                                      String tradePartnerName, String dealingThingName, double net23, double net8,
                                      double net5, double vat23, double vat8, double vat5, double gross){

        if (dateOfIssue.contains("-")){
            String [] parts = dateOfIssue.split("-");
            dateOfIssue = parts[0] + "/" + parts[1] + "/" + parts[2];
        }

        Validator validatorPerson = new Validator();
        if(!validatorPerson.NIPIsValid(tradePartnerNIP)){
            logger.info("Invalid tradePartner NIP!");
            return "Błędne dane: taki NIP nie istnieje.";
        }

        Date date;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd").parse(dateOfIssue);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            int year = localDate.getYear();
            String recordName = month + "/" + year; //must reckon tradeInvoice in the month of trade
            TradeRecord tradeRecord = recordDao.getTradeRecord(recordName,username);

            if (tradeRecord ==  null){ //create monthly reckoning, trade and purchase records
                logger.info("Create new monthlyReckoning, tradeRecord and purchaseRecord.");

                if(!reckoningGenerator.createRecords(username, recordName)){
                    logger.info("Error by create new monthlyReckoning!");
                    return "Przepraszamy błąd podczas połączenia z bazą danych i przypisywania faktury do rejestru";
                }
                tradeRecord =  recordDao.getTradeRecord(recordName, username);
            }



            TradeInvoice tradeInvoice = new TradeInvoice(invoiceNumber, date, Long.valueOf(tradePartnerNIP), tradePartnerName,
                dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross, tradeRecord);

            if (validator.isTradeInvoiceInDB(tradeInvoice.getInvoiceNumber(),username)){
                logger.info("Such tradeInvoice: " + tradeInvoice.getInvoiceNumber() + " already exists in DB");
                return "Taka faktura sprzedaży istnieje już w systemie! Istnieje jedynie możliwość" +
                    " jej edycji lub usunięcia.";
            }

            if(!validator.isValidDateOfTradeInvoice(localDate)){
                return "Faktury z taką datą wystawienia: " + localDate + " nie można rozliczyć! Proszę sprawdzić, " +
                        "czy data jest prawidłowa";
            }

            if(!validator.isValidValuesOfVAT(net5, vat5)){
                return "Błędne dane dla kwoty podatku 5%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(net8, vat8)){
                return "Błędne dane dla kwoty podatku 8%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(net23, vat23)){
                return "Błędne dane dla kwoty podatku 23%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(gross == 0){
                return "Nie można dodać do rejestru faktury z zerową wartością kwoty brutto!";
            }

            if(invoiceDao.addTradeInvoice(tradeInvoice)){
                return "SUCCESS";
            }

        }catch(ParseException pe){
            logger.info("Error by date parsing");
            return "Błędny format daty, wymagany: RRRR/MM/DD";
        }

        return "Przepraszamy, wystąpił błąd techniczny. Faktura nie została zapisana. Prosimy spróbować ponownie.";
    }


    public String addPurchaseInvoiceToDB(String username, String invoiceNumber, String dateOfIssue, String tradePartnerNIP,
                                         String tradePartnerName, String dealingThingName, double net23, double net8, double net5,
                                         double vat23, double vat8, double vat5, double gross, boolean deducted,
                                         double fixedAssetsNet, double fixedAssetsVat, double fixedAssetsGross){

        if (dateOfIssue.contains("-")){
            String [] parts = dateOfIssue.split("-");
            dateOfIssue = parts[0] + "/" + parts[1] + "/" + parts[2];
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd").parse(dateOfIssue);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String recordName = validator.findNameOfRecordForPurchaseInvoice(localDate);
            if (!recordName.contains("/")){
                return recordName; //information about error
            }

            Validator validatorPerson = new Validator();
            if(!validatorPerson.NIPIsValid(tradePartnerNIP)){
                logger.info("Invalid tradePartner NIP!");
                return "Błędne dane: taki NIP nie istnieje.";
            }

            if(validator.isPurchaseInvoiceInDB(invoiceNumber, tradePartnerNIP)){
                logger.info("Such invoice: " + invoiceNumber + " from NIP: " + tradePartnerNIP + " already exists in DB.");
                return "Taka faktura istnieje już w bazie! Nie można dodać jej ponownie.";
            }

            PurchaseRecord purchaseRecord = recordDao.getPurchaseRecord(recordName, username);
            if (purchaseRecord == null){//create monthly reckoning, trade and purchase records
                logger.info("Create new monthlyReckoning, tradeRecord and purchaseRecord.");

                if(!reckoningGenerator.createRecords(username, recordName)){
                    logger.info("Error by create new monthlyReckoning!");
                    return "Przepraszamy błąd podczas połączenia z bazą danych i przypisywania faktury do rejestru";
                }
                purchaseRecord = recordDao.getPurchaseRecord(recordName, username);
            }

            PurchaseInvoice purchaseInvoice = new PurchaseInvoice(invoiceNumber, date, Long.valueOf(tradePartnerNIP),
                    tradePartnerName, dealingThingName, net23, net8, net5, vat23, vat8, vat5, gross, deducted,
                    fixedAssetsNet, fixedAssetsVat, fixedAssetsGross, purchaseRecord);

            if(!validator.isValidValuesOfVAT(net5, vat5)){
                return "Błędne dane dla kwoty podatku 5%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(net8, vat8)){
                return "Błędne dane dla kwoty podatku 8%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(net23, vat23)){
                return "Błędne dane dla kwoty podatku 23%: jedna z wartości: netto lub VAT  jest zerowa a druga nie.";
            }

            if(gross == 0){
                return "Nie można dodać do rejestru faktury z zerową wartością kwoty brutto!";
            }

            if(!validator.isValidValuesOfVAT(fixedAssetsNet, fixedAssetsVat)){
                return "Błędne dane dla kwoty podatku przy środkach trwałych: jedna z wartości: netto lub VAT jest zerowa a druga nie.";
            }

            if(!validator.isValidValuesOfVAT(fixedAssetsNet, fixedAssetsGross)){
                return "Błędne dane dla kwot przy środkach trwałych: jedna z wartości: netto lub brutto jest zerowa a druga nie.";
            }

            if(invoiceDao.addPurchaseInvoice(purchaseInvoice)){
                return "SUCCESS";
            }
            else{
                return "Przepraszamy, wystąpił błąd podczas połączenia z bazą danych. Faktura nie została dodana do rejestru";
            }

        }catch(ParseException pe){
            logger.info("Error by date parsing");
            return "Błędny format daty, wymagany: RRRR/MM/DD";
        }
    }



    /*leak of invoices -> return empty ArrayList
    error by connection with DB -> return null
    ok -> return ArrayList sorted by date of issue */
    public ArrayList<TradeInvoice> getTradeInvoices(String userName, String tradeRecordName){
        ArrayList<TradeInvoice> invoices = new ArrayList<>(invoiceDao.getTradeInvoices(userName, tradeRecordName));
        invoices.sort(new TradeInvoiceComparator());
        invoices.forEach(in -> logger.info("Invoice nr: " + in.getInvoiceNumber() + " date: " + in.getDateOfIssue().toString()));
        return invoices;
    }

    /*leak of invoices -> return empty ArrayList
    error by connection with DB -> return null
    ok -> return ArrayList sorted by date of issue */
    public ArrayList<PurchaseInvoice> getPurchaseInvoices(String userName, String purchaseRecordName){
        ArrayList<PurchaseInvoice> invoices = new ArrayList<>(invoiceDao.getPurchaseInvoices(userName, purchaseRecordName));
        invoices.sort(new PurchaseInvoiceComparator());
        invoices.forEach(in -> logger.info("Invoice nr: " + in.getInvoiceNumber() + " date: " + in.getDateOfIssue().toString()));
        return invoices;
    }

    public String deleteTradeInvoiceFromDB(String invoiceNumber, String username, String invoiceId){
        InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();

        if(!invoiceDao.isTradeInvoiceInDB(invoiceNumber, username)){
            logger.info("Such invoice doesn't exist: invoiceNumber: " + invoiceNumber + " for user: " + username);
            return "Usunięcie nie jest możlwie. Taka faktura nie istnieje w bazie lub należy do innego użytkownika.";
        }

        if(!invoiceDao.deleteTradeInvoice(invoiceId)){
            logger.info("Error by connection with DB");
            return "Przepraszamy, problemy techniczne. Prosimy spórbować ponownie";
        }

        return "SUCCESS";
    }

    public String deletePurchaseInvoiceFromDB(String invoiceNumber, String username, String invoiceId, String invoiceNIP) {
        InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();

        if(!invoiceDao.isPurchaseInvoiceInDB(invoiceNumber, Long.valueOf(invoiceNIP))){
            logger.info("Such invoice doesn't exist: invoiceNumber: " + invoiceNumber + " for user: " + username);
            return "Usunięcie nie jest możlwie. Taka faktura nie istnieje w bazie.";
        }

        if(!invoiceDao.deletePurchaseInvoice(invoiceId)){
            logger.info("Error by connection with DB");
            return "Przepraszamy, problemy techniczne. Prosimy spórbować ponownie";
        }
        return "SUCCESS";
    }

    public TradeRecord getTradeRecord(String username, String recordName){
        return recordDao.getTradeRecord(recordName, username);
    }

    public PurchaseRecord getPurchaseRecord(String username, String purchaseRecordName) {
        return recordDao.getPurchaseRecord(purchaseRecordName, username);
    }

    public Map<String, Integer> generateMonthlyReckoning(String userName, String reckoningName) {

        ReckoningGenerator reckoningGenerator = new ReckoningGenerator();
        return reckoningGenerator.generateReckoning(userName, reckoningName);
    }


    public String validateDeclarationData(Integer overhang, String sumForClient, String in25days, String in60days, String in180days) {
        return declarationValidator.validateDeclarationData(overhang, sumForClient, in25days, in60days, in180days);
    }

    public boolean isOldReckoning(String reckoningName){
        return declarationValidator.isOldReckoning(reckoningName);
    }

    public boolean isReckoningFromTheFuture(String reckoningName){
        return declarationValidator.isReckoningFromTheFuture(reckoningName);
    }

    public Map<String, Integer> updateMonthlyReckoning(Map<String, Integer> preDeclaration, String sumForClient,
                                                       String in25days, String in60days, String in180days,
                                                       String reckoningName, String username) {

        preDeclaration.put("sumRefund", Integer.valueOf(sumForClient));
        preDeclaration.put("refund25Days", Integer.valueOf(in25days));
        preDeclaration.put("refund60Days", Integer.valueOf(in60days));
        preDeclaration.put("refund180Days", Integer.valueOf(in180days));
        Integer forNextMonth = preDeclaration.get("overhang") - preDeclaration.get("sumRefund");
        preDeclaration.put("forNextMonth", forNextMonth);


        MonthlyReckoning monthlyReckoning = monthlyReckoningDao.getMonthlyReckoning(username, reckoningName);
        monthlyReckoning.setForNextMonth(forNextMonth);
        monthlyReckoning.setForRevenue(0);
        monthlyReckoning.setRefund25Days(Integer.valueOf(in25days));
        monthlyReckoning.setRefund60Days(Integer.valueOf(in60days));
        monthlyReckoning.setRefund180Days(Integer.valueOf(in180days));

        DBOperations dbOperations = new DBOperations();
        if(!dbOperations.updateInDB(monthlyReckoning)){
            logger.info("Error by updating final monthly reckoning");
            return null;
        }

        return preDeclaration;
    }

    public MonthlyReckoning getMonthlyReckoning(String username, String reckoningName){
        return monthlyReckoningDao.getMonthlyReckoning(username, reckoningName);
    }
}

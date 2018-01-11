package com.example.demo.user.operation.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

public class DeclarationValidator {

    private Logger logger = Logger.getAnonymousLogger();

    public String validateDeclarationData(Integer overhang, String sumForClient, String in25days, String in60days, String in180days) {
        if(overhang == 0){
            logger.info("Refund for client is impossible. Overhang = 0.");
            return "Nadwyżka podaktu należnego nad naliczonym. Brak możliwości otrzymania zwrotu podatku.";
        }

        if(Integer.valueOf(sumForClient) > overhang){
            logger.info("Sum for client invalid > overhang");
            return "Suma do zwrotu na rachunek bankowy nie może przekraczać nadwyżki podatku = " + overhang;
        }

        Integer sumOfRefund = Integer.valueOf(in25days) + Integer.valueOf(in60days) + Integer.valueOf(in180days);
        if(!sumOfRefund.equals(Integer.valueOf(sumForClient))){
            logger.info("Sum of refund (in 25, 60 and 180 days) must be equals to sumForClient");
            return "Suma zwrotów w terminach: 25, 60, 180 dni, podana jako: "  + sumOfRefund +
                    " musi być zgodna  z całkowitą sumą zwrotu = " + sumForClient;
        }
        return "VALID";
    }


    public boolean isOldReckoning(String reckoningName){
        Date dateNow =  new Date();
        LocalDate localDateNow = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String [] partsOfName = reckoningName.split("/");
        int reckoningMonth = Integer.valueOf(partsOfName[0]);
        int reckoningYear = Integer.valueOf(partsOfName[1]);
        int monthNow = localDateNow.getMonthValue();
        int yearNow = localDateNow.getYear();

        if(localDateNow.getDayOfMonth() <= 25){ //reckoning from this or last month isn't old
            if(monthNow == 1){ //January now
                return !((monthNow == reckoningMonth && yearNow == reckoningYear) || (reckoningMonth == 12 && reckoningYear == yearNow-1));
            }
            return !((monthNow == reckoningMonth && yearNow  == reckoningYear) ||(reckoningMonth == monthNow -1 && reckoningYear == yearNow));
        }
        //only reckoning from this month isn't old
        return !(reckoningMonth == monthNow && reckoningYear == yearNow);
    }

    public boolean isReckoningFromTheFuture(String reckoningName){
        Date dateNow =  new Date();
        LocalDate localDateNow = dateNow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String [] partsOfName = reckoningName.split("/");
        int reckoningMonth = Integer.valueOf(partsOfName[0]);
        int reckoningYear = Integer.valueOf(partsOfName[1]);
        int monthNow = localDateNow.getMonthValue();
        int yearNow = localDateNow.getYear();

       return (reckoningMonth > monthNow && reckoningYear == yearNow) || (reckoningYear > yearNow);
    }

}

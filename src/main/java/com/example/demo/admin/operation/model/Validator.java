package com.example.demo.admin.operation.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import com.example.demo.users.dao.UserDaoImpl;
import org.apache.commons.validator.routines.EmailValidator;

/*validation of new user registration form*/

public class Validator {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    public boolean loginIsUnique(String login){
        LOGGER.info("Validate login");
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        return !userDaoImpl.isUserInDB(login);
    }

    //validate strength of password: min 8 signs (1 lower case letter, 1 upper case letter,
    // 1 digit and 1 special character: @#$%^&+
    public boolean passwordIsStrong(String password){
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    public boolean passwordsAreTheSame(String password, String password2){
        return password.equals(password2);
    }

    //validate names: only letters and spaces are acceptable
    public boolean namesAreValid(String name){
        String[] names = name.split("\\s+");
        for (String n: names) {
            if (!n.chars().allMatch(Character::isLetter)) {
                return false;
            }
        }
        return true;
    }

    //validate surnames: letters, spaces and '-' are acceptable
    public boolean surnamesAreValid(String surname){
        surname = surname.trim();
        String[] surnames = surname.split("-");
        for (String s: surnames) {
            if(!s.chars().allMatch(Character::isLetter)) {
                return false;
            }
        }
        return true;
    }

    //validated by apache.commons.validator.routines.EmailValidator
    public boolean emailIsValid(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    //validate NIP - 10 digits are required
    public boolean NIPIsValid(String NIP){
        return NIP.length()==10 && NIP.chars().allMatch(Character::isDigit);
    }

    //validate phone number - 9 digits are required
    public boolean phoneNrIsValid(String phoneNr){
        return  phoneNr.length() == 9 && phoneNr.chars().allMatch(Character::isDigit);
    }

    //rrrr/mm/dd is valid format
    public boolean dateOfBirthIsValid(String dateToValidate){
        if (dateToValidate == null){
            return false;
        }
        if (dateToValidate.contains("-")){
            String [] parts = dateToValidate.split("-");
            dateToValidate = parts[0] + "/" + parts[1] + "/" + parts[2];
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}

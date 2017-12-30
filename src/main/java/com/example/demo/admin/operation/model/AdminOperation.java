package com.example.demo.admin.operation.model;

import com.example.demo.config.SpringSecurityConfig;
import com.example.demo.persons.dao.PersonDaoImpl;
import com.example.demo.persons.model.Person;
import com.example.demo.users.dao.UserDao;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import com.example.demo.users.model.UserRole;
import com.example.demo.users.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class AdminOperation {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private PersonDaoImpl personDao = new PersonDaoImpl();
    private UserDaoImpl userDao = new UserDaoImpl();
    private Validator validator = new Validator();

    @Transactional
    public String addNewUser(String login, String password, String password2, String email,
                              String name, String surname, String address, String NIP,
                              String dateOfBirth, String phoneNr, String nameOfRevenue){
        System.out.println("Dodaje nowego usera!");
        String result = "SUCCESS";

        //validate login
        if (validator.loginIsUnique(login)) {
            LOGGER.info("Login is unique");
        }
        else{
            result = "Taki login juz instnieje. Proszę podać unikalny login.";
            LOGGER.info("Such login: " + login +  " already exists in DB");
            return result;
        }

        //validate password
        if(validator.passwordIsStrong(password)){
            LOGGER.info("Password is enough strong");
        }
        else{
            result = "Hasło zbyt słabe. Wymagane min 8 znaków: 1 mała litera, 1 wielka, 1 cyfra oraz " +
                    "1 znak specjalny";
            LOGGER.info("Password too leak");
            return result;
        }
        if(validator.passwordsAreTheSame(password, password2)){
            LOGGER.info("Passwords are the same");
        }
        else{
            result = "Hasła muszą być identyczne";
            LOGGER.info("Password aren't the same");
            return result;
        }

        //validate name
        if(validator.namesAreValid(name)){
            LOGGER.info("Names are correct");
        }
        else{
            result = "Imiona powinny składać się wyłącznie z liter.";
            LOGGER.info("Unexpected signs in names");
            return result;
        }

        //validate surname
        if(validator.surnamesAreValid(surname)){
            LOGGER.info("Names are correct");
        }
        else{
            result = "Imiona powinny składać się wyłącznie z liter.";
            LOGGER.info("Unexpected signs in names");
            return result;
        }

        //validate email
        if(validator.emailIsValid(email)){
            LOGGER.info("Email address is valid");
        }
        else{
            result = "Taki adres email nie istnieje";
            LOGGER.info("Email address are not valid.");
            return result;
        }

        //validate NIP
        if(validator.NIPIsValid(NIP)){
            LOGGER.info("NIP is ok");
        }
        else{
            result = "Błędny NIP";
            LOGGER.info("NIP is not correct");
            return result;
        }

        //validate phone number
        if(validator.phoneNrIsValid(phoneNr)){
            LOGGER.info("Phone number is valid");
        }
        else{
            result = "Numer telefonu niepoprawny. Wymagane 9 cyfr.";
            LOGGER.info("Phone number is not valid");
            return result;
        }

        //validate date of birth:
        if(validator.dateOfBirthIsValid(dateOfBirth)){
            LOGGER.info("Date of birth in valid format");
        }
        else{
            result = "Date of birth has invalid format. Required: RRRR-MM-DD";
            LOGGER.info("Date of birth has not valid format");
            return result;
        }

        SpringSecurityConfig security = new SpringSecurityConfig();
        PasswordEncoder passwordEncoder = security.passwordEncoder();
        String hashPassword = passwordEncoder.encode(password);

        User user = new User(login, hashPassword, true);
        UserRole userRole = new UserRole(user, "ROLE_USER");

        if (dateOfBirth.contains("-")){
            String [] parts = dateOfBirth.split("-");
            dateOfBirth = parts[0] + "/" + parts[1] + "/" + parts[2];
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd").parse(dateOfBirth);
        }catch(ParseException pe){
            LOGGER.info("Error by date parsing");
            return"Date of birth has invalid format. Required: RRRR-MM-DD";
        }

        Person person = new Person(login, name, surname, address, email, Long.valueOf(NIP),
                Long.valueOf(phoneNr), date, nameOfRevenue, user);

        UserDaoImpl userDao = new UserDaoImpl();
        PersonDaoImpl personDao = new PersonDaoImpl();
        if (!userDao.newUserRegistration(user, userRole) || !personDao.addNewPerson(person)){
            result = "Przepraszamy. Błąd podczas połączenia z bazą danych.";
            LOGGER.info("Error by connection with MySQL DB");
        }

        return result;
    }

     public List<Person> getPersonsList(){
         return personDao.getPersonsList();
     }

     public boolean deleteUser(String username){
          return (personDao.deletePerson(username) && userDao.deleteUserRoles(username)
                  && userDao.deleteUser(username));
     }

     /*return information about error or SUCCESS if transaction was correct*/
     public String changePassword(String username, String currentPassword, String newPassword, String newReplacedPassword){
         if(!userDao.isUserInDB(username)){
             LOGGER.info("Such username: " + username +  " doesn't exist in DB");
             return "Brak takiego użytkownika w bazie.";
         }

         if(!userDao.isCorrectPassword(username, currentPassword)){
             LOGGER.info("Password is incorrect");
             return "Podane obecne hasło nie jest poprawne.";
         }

         if(!validator.passwordsAreTheSame(newPassword, newReplacedPassword)){
             LOGGER.info("New password and new replaced password aren't the same");
             return "Nowe hasło i jego powtórzenie nie są zgodne.";
         }
         if(!validator.passwordIsStrong(newPassword)){
             LOGGER.info("New password isn't enough strong");
             return "Hasło zbyt słabe. Wymagane min 8 znaków: 1 mała litera, 1 wielka, 1 cyfra oraz " +
             "1 znak specjalny";
         }
         if(!userDao.changePassword(username, newPassword)){
             LOGGER.info("Error by connection with DB");
             return"Przepraszamy. Błąd podczas połączenia z bazą dancyh.";
         }
         return "SUCCESS";
     }

}

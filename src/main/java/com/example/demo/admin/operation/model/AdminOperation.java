package com.example.demo.admin.operation.model;

import com.example.demo.config.SpringSecurityConfig;
import com.example.demo.users.dao.UserDao;
import com.example.demo.users.dao.UserDaoImpl;
import com.example.demo.users.model.User;
import com.example.demo.users.model.UserRole;
import com.example.demo.users.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.logging.Logger;

public class AdminOperation {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");

//    @Autowired
//    private PasswordEncoder passwordEncoder = new PasswordEncoder() {
//                @Override
//        public String encode(CharSequence charSequence) {
//            return null;
//        }
//
//        @Override
//        public boolean matches(CharSequence charSequence, String s) {
//            return false;
//        }
//    };

    @Autowired
    private UserDao userDao;


    @Transactional
    public boolean addNewUser(String login, String password, String password2, String email,
                              String name, String surname, String address, String NIP,
                              String dateOfBirth, String phoneNr, String nameOfRevenue){
        System.out.println("Dodaje nowego usera!");
        boolean result = false;
        Validator validator = new Validator();
        if (validator.loginIsUnique(login)){
            LOGGER.info("Login is unique");

            SpringSecurityConfig security = new SpringSecurityConfig();
            PasswordEncoder passwordEncoder = security.passwordEncoder();
            String hashPassword = passwordEncoder.encode(password);

            User user = new User(login, hashPassword, true);
            UserRole userRole = new UserRole(user, "ROLE_USER");
            UserDaoImpl userDao = new UserDaoImpl();
            result = userDao.newUserRegistration(user, userRole);
        }
        return result;
    }
}

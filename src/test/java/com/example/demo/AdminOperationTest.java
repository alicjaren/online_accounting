package com.example.demo;

import com.example.demo.admin.operation.model.AdminOperation;
import com.example.demo.config.SpringSecurityConfig;
import com.example.demo.persons.dao.PersonDaoImpl;
import com.example.demo.users.dao.UserDaoImpl;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class AdminOperationTest {

    private PersonDaoImpl personDao = new PersonDaoImpl();
    private UserDaoImpl userDao = new UserDaoImpl();
    private AdminOperation admin = new AdminOperation();

    @Test
    public void findPersonByUserNameTest(){
        assertNotNull(personDao.findByUserName("user2"));
        assertNull(personDao.findByUserName("user-1"));
    }

    @Test
    public void deleteUserTest(){
        //assertTrue(userDao.deleteUser("user3"));
        assertFalse(userDao.deleteUser("user3"));
    }

    @Test
    public void deleteUserRolesTest(){
        assertTrue(userDao.deleteUserRoles("userNew1"));
        assertTrue(userDao.deleteUserRoles("userNew1"));
        //assertTrue(userDao.deleteUserRoles("userNew123"));
        assertFalse(userDao.deleteUserRoles("user3"));
    }

    @Test
    public void deletePersonTest(){
        //assertTrue(personDao.deletePerson("userNew123"));
        assertFalse(personDao.deletePerson("user-1"));
    }

    @Test
    public void deleteUserWithRolesTest() {
        assertTrue(admin.deleteUser("userNew123"));
        assertFalse(admin.deleteUser("userNew123"));
    }

    @Test
    public void changePasswordTest(){

        SpringSecurityConfig security = new SpringSecurityConfig();
        PasswordEncoder passwordEncoder = security.passwordEncoder();
        String hashPassword = passwordEncoder.encode("qwQW1234@");
        Logger logger = Logger.getLogger("hash: " + hashPassword);

        assertTrue(userDao.isCorrectPassword("user4","qwQW1234@"));
        assertTrue(userDao.changePassword("user4", "qwQW1234@"));
        assertFalse(userDao.changePassword("usr5", "password")); //user doesn't exists
        assertEquals(admin.changePassword("user4", "qwQW1234@",
                "qwQW1234@", "qwQW1234@"),"SUCCESS");
        assertEquals(admin.changePassword("user4", "qwQW1234",
                "qwQW1234@", "qwQW1234@"),"Podane obecne hasło nie jest poprawne."); //incorrect current password
        assertEquals(admin.changePassword("user4", "qwQW1234@",
                "qwQW1234@", "qwQW123"),"Nowe hasło i jego powtórzenie nie są zgodne.");//new passwords aren't the same

    }

}

package com.example.demo;

import com.example.demo.admin.operation.model.AdminOperation;
import com.example.demo.admin.operation.model.Validator;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorTest {

    private Validator validator = new Validator();
    private AdminOperation admin = new AdminOperation();

    @Test
    public void testLoginIsUnique(){
        assertTrue(validator.loginIsUnique("userReallyNew"));
        assertFalse(validator.loginIsUnique("user1"));
    }

    @Test
    public void testPasswordIsStrong(){
        assertFalse(validator.passwordIsStrong("qwQ1@")); //too short
        assertFalse(validator.passwordIsStrong("qwQ111111111111")); //without special character
        assertFalse(validator.passwordIsStrong("qwQWWWWW!!!!!!!")); //without digit
        assertFalse(validator.passwordIsStrong("qqqwqq111!!!!")); //without upper case
        assertFalse(validator.passwordIsStrong("QQQQQ11111!!!!!1")); //without lower case
        assertFalse(validator.passwordIsStrong("qqQ!!1 1"));//with whitespace
        assertTrue(validator.passwordIsStrong("qwQQ@@@@@2q"));//strong password
    }

    @Test
    public void testPasswordAreTheSame(){
        assertTrue(validator.passwordsAreTheSame("123", "123"));
        assertFalse(validator.passwordsAreTheSame("abc", "ab"));
    }

    @Test
    public void testNIPIsValid(){
        assertFalse(validator.NIPIsValid("1111111")); //too short
        assertFalse(validator.NIPIsValid("1111111111111aaaaaaaaaaa"));//not only digits
        assertFalse(validator.NIPIsValid("12345678900"));//too long
        assertTrue(validator.NIPIsValid("1234567890"));//valid NIP
    }

    @Test
    public void testPhoneNrIsValid(){
        assertFalse(validator.phoneNrIsValid("11111"));//too short
        assertFalse(validator.phoneNrIsValid("1111aaaaa"));//not only digits
        assertFalse(validator.phoneNrIsValid("1234567890")); //too long
        assertTrue(validator.phoneNrIsValid("123456789"));//valid phone number
    }

    @Test
    public void testNamesAreValid(){
        assertFalse(validator.namesAreValid("łukasz123 12"));//not only letters and spaces
        assertTrue(validator.namesAreValid("Adaś łukasz"));//valid name
    }

    @Test
    public void testSurnamesAreValid(){
        assertFalse(validator.surnamesAreValid("Gdldd ddd 12221"));//not only letters, spaces and '-'
        assertTrue(validator.surnamesAreValid("Kowalski-Ćwiek"));//valid surname
        assertTrue(validator.surnamesAreValid("Kowalski"));//valid surname
    }

    @Test
    public void testEmailIsValid(){
        assertFalse(validator.emailIsValid("ggg223232.pl"));
        assertFalse(validator.emailIsValid("sddmail@"));
        assertTrue(validator.emailIsValid("alice_456@wp.pl"));
    }

    @Test
    public void testDateOfBirthIsValid(){
        assertFalse(validator.dateOfBirthIsValid("2000.11.01"));//invalid format
        assertFalse(validator.dateOfBirthIsValid("1988/99/01"));//invalid month
        assertFalse(validator.dateOfBirthIsValid("1997/32/10"));//invalid month
        assertFalse(validator.dateOfBirthIsValid("1998-09-32"));//invalid day
        assertTrue(validator.dateOfBirthIsValid("2001-02-11"));//valid format 2000+
        assertTrue(validator.dateOfBirthIsValid("1996/02/19"));//valid format 1900+
    }

    @Test
    public void testAddNewUser(){
        /*assertEquals(admin.addNewUser("userNew", "12@qwWW1qq","12@qwWW1qq",
                "userNew@mail.pl", "Adaś Michał", "Kowalski-Myśliwski",
                "ul.Kwiatowa 10 06-400 Ciechanów", "1234567890","1988-01-09",
                "333444555","Urząd Skarbowy w Ciechanowie") , "SUCCESS");*/

        assertEquals(admin.addNewUser("userNew", "12@qwWW1qq","12@qwWW1qq",
                "userNew@mail.pl", "Adaś Michał", "Kowalski-Myśliwski",
                "ul.Kwiatowa 10 06-400 Ciechanów", "1234567890","1988-01-09",
                "333444555","Urząd Skarbowy w Ciechanowie") ,
                "Taki login juz instnieje. Proszę podać unikalny login.");
    }
}

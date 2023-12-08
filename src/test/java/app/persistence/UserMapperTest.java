package app.persistence;

import app.model.entities.Customer;
import app.model.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMapperTest {
    int id;
    User name;
    User name2;
    User email;
    String password;
    double balance;


    @BeforeEach
    void setUp() {
        name = new Customer(1,"Alexander", "Rasmussen", "ba@gmail.com", "1234", "Admin", 20);
    }

    @AfterEach
    void tearDown() {
        name = null;
    }

    @Test
    void login() {
        /*String expected1 = "Alex";
        String expected2 = "1234";

        String actual1 = name.getName();
        String actual2 = name.getPassword();
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);*/

        

    }

    @Test
    void createUser() {
    }
}
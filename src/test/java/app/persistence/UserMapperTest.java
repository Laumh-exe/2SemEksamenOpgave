package app.persistence;

import app.entities.Customer;
import app.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    User name;
    User name2;
    int id;
    String password;
    double balance;


    @BeforeEach
    void setUp() {
        name = new Customer(1,"Alex", "1234", "Admin", 20);
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
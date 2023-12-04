package app.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
User name;
User name2;
int id;
String password;

    @BeforeEach
    void setUp() {
        name = new User(1, "Alex", "1234", "Admin", 5);
    }

    @AfterEach
    void tearDown() {
        name = null;
        name2 = null;
        id = 1;
        password = null;
    }

    @Test
    void getId() {
        int expected = 1;
        int actual = name.getId();
        assertEquals(expected, actual);
    }

    @Test
    void getName() {
        String expected = "Valde";
        name2 = new User(1, "Valde", "1234", "Kunde", 20);
        String actual = name.getName();
        assertEquals(expected, actual);
    }

    @Test
    void getPassword() {
        String expected1 = "1234";
        String actual1 = name.getPassword();
        assertEquals(expected1, actual1);

        String expected2 = "1235";
        name = new User(1, "Alex", "1235", "Admin", 5);
        String actual2 = name.getPassword();
        assertEquals(expected2, actual2);
    }

    @Test
    void getRank() {
        /*String expected = "Admin";
        name2 = new User(2, "Valde", "1235", "Kunde", 20);
        String actual = name2.getRank();
        assertEquals(expected, actual); */

        String expected2 = "Admin";
        name = new User(1, "Alex", "1234", "Admin", 5);
        String actual2 = name.getRank();
        assertEquals(expected2,actual2);
    }

    @Test
    void getBalance() {
        /*double expected = 20;
        double actual = name.getBalance();
        assertEquals(expected, actual);*/

        double expected2 = 20;
        name = new User(2,"Valde","1235","Kunde",20);
        double actual2 = name.getBalance();
        assertEquals(expected2, actual2);
    }

    @Test
    void setBalance() {
    }
}
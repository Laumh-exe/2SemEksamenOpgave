package app.entities;

import app.model.entities.Customer;
import app.model.entities.User;
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

        name = new Customer(1, "Alexander", "Rasmussen", "email@email.com", "1234", "Admin");
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
    void getfirstName() {
        String expected1 = "Val";
        name2 = new Customer(1, "Val", "de", "jaja@gmail.com", "1235", "Kunde");
        String actual = name2.getFirstName();
        assertEquals(expected1, actual);
    }

    @Test
    void getlastName(){
        String expected = "de";
        name2 = new Customer(1, "Val", "de", "jaja@gmail.com", "1235", "Kunde");
        String actual = name2.getLastName();
        assertEquals(expected, actual);
    }

    @Test
    void getPassword() {
        String expected1 = "1234";
        String actual1 = name.getPassword();
        assertEquals(expected1, actual1);

        String expected2 = "1234";
        name = new Customer(1, "Alexander", "Rasmussen", "ba@gmail.com", "1234", "Admin");
        String actual2 = name.getPassword();
        assertEquals(expected2, actual2);
    }

    @Test
    void getRole() {
        /*String expected = "Admin";
        name2 = new User(2, "Valde", "1235", "Kunde");
        String actual = name2.getRole();
        assertEquals(expected, actual); */

        String expected2 = "Admin";
        name = new Customer(1, "Alexander", "Rasmussen", "ba@gmail.com", "1234", "Admin");
        String actual2 = name.getRole();
        assertEquals(expected2,actual2);
    }

    // @Test
    // void getBalance() {
    //     /*double expected = 20;
    //     double actual = name.getBalance();
    //     assertEquals(expected, actual);*/

    //     double expected2 = 20;
    //     name2 = new Customer(2,"Valde", "Valde", "jaja@gmail.com","1235","Kunde");
    //     double actual2 = name2.getBalance();
    //     assertEquals(expected2, actual2);
    // }

    // @Test
    // void setBalance() {
    //     double expected = 5;
    //     double actual = name.setBalance(5);
    //     assertEquals(expected, actual);
    // }
}
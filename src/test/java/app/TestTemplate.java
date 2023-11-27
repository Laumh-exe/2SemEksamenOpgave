package app;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TestTemplate
{
    @BeforeEach
    public void setup(){
        /**
        * Here you put anything that needs to be made before your test
        * Ex:
        * user = new User()
        */
    }

    @AfterEach
    public void teardown(){
        /**
         * Here you put the deststruction of ALL the entityes you made in setup
         * Ex:
         * user = null
         */
    }

    /**
     * This is an example test is not garenteed to look like the test you need to make
     */
    @Test
    public void testTemplate(){
        // Arrange
        String expected = "test";
        String actual = null;

        // Act 
        actual = TestTemplate.testString();
        
        // Assert
        assertEquals(expected,actual);
    }

    private static String testString(){
        return "test";
    }
}

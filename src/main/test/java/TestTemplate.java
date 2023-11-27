package app;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is for demonstration purposes
 */
public class TestTemplate {
    @BeforeEach
    public void setup(){
        System.out.println("hello?");
    }

    /**
     * This is an example test
     */
    @Test
    private void testTemplate(){
        // Arrange
        String actual = null;
        String expected = "test";

        // Act
        actual = TestTemplate.methodToTest();
        // In an actual test you need to use the metod from the class you are testing
        // Example if there was a 'test' metod in main that returns the string "test":
        // actual = Main.test();


        // Assert
        assertEquals(expected,actual);
    }

    /**
     * This method is for the test to compile and shuld be the a refeance to the actual method you want to test
     */
    public static String methodToTest() {
        return "testt";
    }


}

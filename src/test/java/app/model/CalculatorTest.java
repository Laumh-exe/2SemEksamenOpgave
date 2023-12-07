package app.model;

import app.TestTemplate;
import app.entities.Carport;
import app.entities.Shed;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CalculatorTest {

    private Shed standardShed;
    private Carport standardCarport;
    @BeforeEach
    public void setup(){
        standardShed = new Shed(6, 2.2);
        standardCarport = new Carport(6 ,7.8, standardShed);
    }

    @AfterEach
    public void teardown(){
        standardShed = null;
        standardCarport = null;
    }

    @Test
    public void calculateItemListTest(){
        // Arrange - ItemList setup


        // Act
        actual = TestTemplate.testString();

        // Assert
        assertEquals(expected,actual);
    }

    private static String testString(){
        return "test";
    }


}
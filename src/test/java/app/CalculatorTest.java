package app;

import app.TestTemplate;
import app.entities.*;
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
        ItemList expectedItemList = new ItemList();
        expectedItemList.add(new Item(100, 360, "Stk", "25x200 mm. trykimp. Bræt| understernbrædder til for & bag ende"), 4);
        expectedItemList.add(new Item(100, 540, "Stk", "25x200 mm. trykimp. Bræt| understernbrædder til siderne"), 4);
        expectedItemList.add(new Item(75, 300, "Stk", "97x97 mm. trykimp. stolpe"), 11);

        // Act
        ItemList actualItemList = Calculator.calculateItemList(standardCarport);

        // Assert
        assertEquals(expectedItemList.getItemList(),actualItemList.getItemList());
    }

    private static String testString(){
        return "test";
    }


}
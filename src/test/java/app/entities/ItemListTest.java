package app.entities;

import app.TestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemListTest {
    Item dummyItem;
    @BeforeEach
    public void setup(){
        dummyItem = new Item(75, 300, "Stk", "97x97 mm. trykimp. stolpe");
    }

    @AfterEach
    public void teardown(){
        dummyItem = null;
    }

    @Test
    public void addAmount3(){
        // Arrange
        ItemList itemList = new ItemList();
        // Act
        itemList.add(dummyItem, 3);

        // Assert
        assertEquals(itemList.getItemList().size(),3);
    }

    private static String testString(){
        return "test";
    }
}
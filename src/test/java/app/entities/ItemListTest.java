package app.entities;

import app.model.entities.Item;
import app.model.entities.ItemList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemListTest {
    Item dummyItem;



    @BeforeEach
    public void setup(){
        dummyItem = new Item(1, 75, 300, "Stk", "97x97 mm. trykimp. stolpe", 5);
    }

    @AfterEach
    public void teardown(){
        dummyItem = null;
    }

    @Test
    public void addAmount5(){
        // Arrange
        ItemList itemList = new ItemList();
        // Act
        itemList.add(dummyItem);

        // Assert
        assertEquals(itemList.getItemList().get(0).quantity(),5);
    }

    private static String testString(){
        return "test";
    }


}
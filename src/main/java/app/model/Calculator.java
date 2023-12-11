package app.model;

import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;

public class Calculator {


    public static ItemList calculateItemList(Carport carport) {
        ItemList itemList = new ItemList();
        if(carport.getLength() == 6 && carport.getWidth() == 7.8 && carport.getShed().getLength() == 6 && carport.getShed().getWidth() == 2.2) {
            itemList.add(new Item(100, 360, "Stk", "25x200 mm. trykimp. Bræt| understernbrædder til for & bag ende"), 4);
            itemList.add(new Item(100, 540, "Stk", "25x200 mm. trykimp. Bræt| understernbrædder til siderne"), 4);
            itemList.add(new Item(75, 300, "Stk", "97x97 mm. trykimp. stolpe"), 11);
        }
        return itemList;
    }
}
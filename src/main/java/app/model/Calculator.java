package app.model;

import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;

public class Calculator {


    public static ItemList calculateItemList(Carport carport) {
        ItemList itemList = new ItemList();
        if(carport.getLength() == 180 && carport.getWidth() == 180 && carport.getShed().getLength() == 180 && carport.getShed().getWidth() == 180) {
            itemList.add(new Item(3, 100, 360, "Stk", "25x200 mm. trykimp. Bræt| understernbrædder til for & bag ende", 4));
            itemList.add(new Item(4, 100, 540, "Stk", "25x200 mm. trykimp. Bræt| understernbrædder til siderne",4));
            itemList.add(new Item(5, 75, 300, "Stk", "97x97 mm. trykimp. stolpe", 11));
        }
        return itemList;
    }
}

package app.model.entities;

import java.util.ArrayList;
import java.util.List;

public class ItemList {
    private List<Item> itemList;

    public ItemList() {
        itemList = new ArrayList<Item>();
    }

    public ItemList(List<Item> itemlist) {
        itemList = itemlist;
    }

    public void add(Item item) {
        itemList.add(item);
    }
    public void addAll(List<Item> list) {
        itemList.addAll(list);
    }



    public void remove(Item item) {
        itemList.remove(item);
    }

    /*
    public void remove(int id) {
        for(Item item : itemList) {
            if(item.Id() == id) {
                itemList.remove(item);
            }
        }
    }

     */

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}

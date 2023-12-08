package app.entities;

public class Carport {
    private double length;
    private double width;

    private Shed shed;
    private ItemList itemList;
    
    public Carport(double length, double width, Shed shed) {
        this.length = length;
        this.width = width;
        this.shed = shed;
    }
    public Carport(double length, double width, Shed shed, ItemList itemList) {
        this.length = length;
        this.width = width;
        this.shed = shed;
        this.itemList = itemList;
    }
    public Carport(double length, double width) {

        this.length = length;
        this.width = width;
        this.shed = shed;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public Shed getShed() {
        return shed;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

}

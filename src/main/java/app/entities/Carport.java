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


}

package app.model.entities;

public class Carport {
    private double lengthMeter;
    private double widthMeter;
    private boolean hasShed;
    private Shed shed;
    private ItemList itemList;
    public Carport(double lengthMeter, double widthMeter, Shed shed) {
        this.lengthMeter = lengthMeter;
        this.widthMeter = widthMeter;
        this.shed = shed;
        hasShed = true;
    }
    public Carport(double lengthMeter, double widthMeter, Shed shed, ItemList itemList) {
        this.lengthMeter = lengthMeter;
        this.widthMeter = widthMeter;
        this.shed = shed;
        this.itemList = itemList;
        hasShed = true;
    }
    public Carport(double lengthMeter, double widthMeter) {

        this.lengthMeter = lengthMeter;
        this.widthMeter = widthMeter;
        hasShed = false;
    }

    public Carport(double lengthMeter, double widthMeter, ItemList itemList) {

        this.lengthMeter = lengthMeter;
        this.widthMeter = widthMeter;
        this.itemList = itemList;
        hasShed = false;
    }

    public boolean hasShed() {
        return hasShed;
    }

    public double getLengthMeter() {
        return lengthMeter;
    }

    public double getWidthMeter() {
        return widthMeter;
    }

    public Shed getShed() {
        return shed;
    }
    public ItemList getItemList() {
        return itemList;
    }

    public void setLengthMeter(double lengthMeter) {
        this.lengthMeter = lengthMeter;
    }

    public void setWidthMeter(double widthMeter) {
        this.widthMeter = widthMeter;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }
    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }
}
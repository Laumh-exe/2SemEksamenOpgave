package app.entities;

public class Carport {
    private double length;
    private double width;

    private Shed shed;
    
    public Carport(double length, double width, Shed shed) {
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

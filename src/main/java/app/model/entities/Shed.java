package app.model.entities;

public class Shed {

    private double lengthMeter;
    private double widthMeter;

    private boolean noCorrectionOfMeter;


    public Shed(double lengthMeter, double widthMeter) {
        this.lengthMeter = lengthMeter;
        this.widthMeter = widthMeter-0.70;
    }

    public Shed(double lengthMeter, double widthMeter, boolean noCorrectionOfMeter) {
        this.lengthMeter = lengthMeter;
        this.widthMeter = widthMeter+0.70;
        this.noCorrectionOfMeter = noCorrectionOfMeter;

    }

    public double getLengthMeter() {
        return lengthMeter;
    }

    public double getWidthMeter() {
        return widthMeter;
    }

    public void setLengthMeter(double lengthMeter) {
        this.lengthMeter = lengthMeter;
    }

    public void setWidthMeter(double widthMeter) {
        this.widthMeter = widthMeter;
    }

}
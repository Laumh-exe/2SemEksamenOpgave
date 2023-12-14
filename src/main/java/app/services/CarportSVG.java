package app.services;

import java.util.List;
import java.util.stream.Collectors;

import app.model.Calculator;
import app.model.entities.Carport;
import app.model.entities.Item;

public class CarportSVG {
    private float width;
    private float height;
    private SVG carportSvg;
    private Carport carport;
    private Calculator calculator;
    private final float spasingFromSides = 100;
    
    public CarportSVG(Carport carport){
        
        this.height = (float)carport.getLength() * 100;
        this.width = (float)carport.getWidth() * 100;
        calculator = Calculator.getInstance();
        this.carport = carport;

        carportSvg = new SVG(0, 0, "0 0 " + (height + spasingFromSides) + " " + (width + spasingFromSides), "75%");
        carportSvg.addRectangle(50, 50, width, height, "stroke: #000000; fill:none");

        addStolber();
        addSpær();
        addArrows();
    }

    private void addArrows() {
        carportSvg.addText(20, height/2, -90, "" + carport.getLength() + "m");
        carportSvg.addArrow(40, 50, 40, width + 50, "stroke: #000000; fill:#000000;");
        carportSvg.addText(width/2, height + 80, 0, "" + carport.getWidth() + "m");
        carportSvg.addArrow(50, height + 60, width, height + 60, "stroke: #000000; fill:#000000;");
    }

    private void addSpær(){
        List<Item> spærList = carport.getItemList().getItemList().stream().filter(a -> a.description().equals("spær")).collect(Collectors.toList());
        int numSpær = calculator.getSpærQuantaty(height);
        float spærDrawWidth = 10;
        float spærSpasing = (height/numSpær) - (spærDrawWidth*2/numSpær);
        for (int i = 0; i < spærList.get(0).quantity(); i++) {
            carportSvg.addRectangle((i * spærSpasing) + spasingFromSides/2 + 5, 50, width, spærDrawWidth, "stroke: #000000; fill:none;");
        }
    }
    
    private void addStolber(){
        Item stolbe = carport.getItemList().getItemList().stream().filter(a -> a.description().equals("stolbe")).collect(Collectors.toList()).get(0);
        float stolbeSpasing = 60; //TODO: get spasing from calculator
        for (int i = 0; i < stolbe.quantity(); i++) {
            carportSvg.addRectangle(i * stolbeSpasing + 50, 50, 20, 20, "stroke: #000000; fill:none");
            carportSvg.addRectangle(i * stolbeSpasing + 50, height, 20, 20, "stroke: #000000; fill:none;");
        }
    }
    
    @Override
    public String toString(){
        return carportSvg.toString();
    }
}

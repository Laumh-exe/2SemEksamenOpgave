package app.services;

import java.util.List;
import java.util.stream.Collectors;

import app.model.entities.Carport;
import app.model.entities.Item;

public class CarportSVG {
    private int width;
    private int height;
    private SVG carportSvg;
    private Carport carport;
    private Calculator calculator;
    private final int spasingFromSides = 100;
    private final int stolbeSpaseFromSides = 35;
    private final float stolbeSize = 20;


    
    public CarportSVG(Carport carport){
        
        this.height = (int)(carport.getLengthMeter() * 100);
        this.width = (int)(carport.getWidthMeter() * 100);
        calculator = Calculator.getInstance();
        calculator.calculateItemList(carport);
        this.carport = carport;

        carportSvg = new SVG(0, 0, "0 0 " + (width + spasingFromSides) + " " + (height + spasingFromSides), "75%");
        carportSvg.addRectangle(spasingFromSides/2, spasingFromSides/2, width, height, "stroke: #000000; fill:none");
        if(carport.hasShed()){
            addSkur();
        }
        addStolber();
        addSpær();
        addRem();
        addArrows();
    }

    private void addSkur() {
        int shedLength = (int)(carport.getShed().getLengthMeter() * 100d);
        int shedWidth = (int)(carport.getShed().getWidthMeter() * 100d);
        carportSvg.addRectangleWithDashedBorder(spasingFromSides/2 + width - (shedWidth + stolbeSpaseFromSides), spasingFromSides/2 + height - shedLength, shedWidth, shedLength);
        if (calculator.getStolpeShedQuantity() == 0) {
            return;
        }
        carportSvg.addRectangle(spasingFromSides/2 + width - (shedWidth + stolbeSpaseFromSides), spasingFromSides/2 + height - shedLength, stolbeSize, stolbeSize);
        carportSvg.addRectangle(spasingFromSides/2 + width - stolbeSpaseFromSides,               spasingFromSides/2 + height - shedLength, stolbeSize, stolbeSize, -stolbeSize, 0d);
        if(calculator.getStolpeShedQuantity() == 2){
            return;
        }
        int numShedStolber = (calculator.getStolpeShedQuantity()-2)/2;
        float stolbeSpasing = (height-stolbeSize)/(numShedStolber) > 250 ? 250 : (shedWidth-stolbeSize)/(numShedStolber);
        for (int i = 0; i < numShedStolber; i++) {
            carportSvg.addRectangle(i * stolbeSpasing + spasingFromSides/2 + stolbeSpasing, spasingFromSides/2 + height - shedLength, stolbeSize, stolbeSize);
            carportSvg.addRectangle(i * stolbeSpasing + spasingFromSides/2 + stolbeSpasing, spasingFromSides/2 + height - 25, stolbeSize, stolbeSize, "stroke: #000000; fill:none;");
        }
    }

    private void addRem() {
        float remWidth = 15;
        
        carportSvg.addRectangle(spasingFromSides/2 + stolbeSpaseFromSides - remWidth/2, spasingFromSides/2, remWidth, height, remWidth/2, 0d);
        carportSvg.addRectangle(width + spasingFromSides/2 - stolbeSpaseFromSides - remWidth/2, spasingFromSides/2, remWidth, height, -remWidth/2, 0d);
    }

    private void addArrows() {
        carportSvg.addText(20, height/2, -90, "" + carport.getLengthMeter() + "m");
        carportSvg.addArrow(40, spasingFromSides/2, 40, height + spasingFromSides/2, "stroke: #000000; fill:#000000;");
        carportSvg.addText(width/2, height + spasingFromSides - 20, 0, "" + carport.getWidthMeter() + "m");
        carportSvg.addArrow(spasingFromSides/2, height + spasingFromSides - 40, width + spasingFromSides/2, height + spasingFromSides - 40  , "stroke: #000000; fill:#000000;");
    }

    private void addSpær(){
        int numSpær = calculator.getSpærQuantity();
        float spærDrawWidth = 10;
        float spærSpasing = (height-spærDrawWidth)/(numSpær-1);
        for (int i = 0; i < numSpær; i++) {
            carportSvg.addRectangle(spasingFromSides/2,i * spærSpasing + spasingFromSides/2, width, spærDrawWidth, "stroke: #000000; fill:none;");
        }
    }
    
    private void addStolber(){

        int numCarportStolber = (calculator.getStolpeQuantity()-calculator.getStolpeShedQuantity())/2;
        float stolbeSpasing = (height-stolbeSize)/(numCarportStolber-1);

        //Hjørne stolber
        carportSvg.addRectangle(spasingFromSides/2 + stolbeSpaseFromSides,          spasingFromSides/2 + 100,         stolbeSize, stolbeSize);
        carportSvg.addRectangle(spasingFromSides/2 + width - stolbeSpaseFromSides,  spasingFromSides/2 + 100,         stolbeSize, stolbeSize, -stolbeSize, 0d);
        carportSvg.addRectangle(spasingFromSides/2 + stolbeSpaseFromSides,          spasingFromSides/2 + height - 25, stolbeSize, stolbeSize, 0d, -stolbeSize);
        carportSvg.addRectangle(spasingFromSides/2 + width - stolbeSpaseFromSides,  spasingFromSides/2 + height - 25, stolbeSize, stolbeSize, -stolbeSize, -stolbeSize);



        for (int i = 1; i < numCarportStolber - 1; i++) {
            carportSvg.addRectangle(spasingFromSides/2 + stolbeSpaseFromSides, i * stolbeSpasing + spasingFromSides/2 + 100, stolbeSize, stolbeSize);
            carportSvg.addRectangle(width + spasingFromSides/2 - stolbeSpaseFromSides, i * stolbeSpasing + spasingFromSides/2 + 100, stolbeSize, stolbeSize, "stroke: #000000; fill:none;", -stolbeSize, 0d);
        }
    }



    
    @Override
    public String toString(){
        return carportSvg.toString();
    }
}

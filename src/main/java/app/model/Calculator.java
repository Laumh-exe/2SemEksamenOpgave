package app.model;

import app.controllers.ItemController;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.round;

/**
 * This is a Singelton
 */
public class Calculator {

    private List<Item> items;
    private static Calculator instance = null;

    /**
     * Setup of Singleton
     * @return this will always return either a new or an existing instance of Calculator
     */
    public static Calculator getInstance() {
        if (instance==null) {
            instance = new Calculator();
        }
        instance.items = ItemController.getAllItems();
        return instance;
    }

    /**
     * Collects itemList data from several methods
     * @param carport
     * @return ItemList with all collected data
     */
    public ItemList calculateItemList(Carport carport) {
        List<Double> spærLengths = getSpærLengths();
        double lowestSpærLength = spærLengths.get(0);
        double highestSpærLength = spærLengths.get(spærLengths.size());
        List<Item> spær = calculateSpær(carport, lowestSpærLength);

        return null;
    }

    /**
     * Get "spær" lengths in a list and sort
     * @return
     */
    private List<Double> getSpærLengths() {
        ArrayList<Double> lengths = new ArrayList<>();
        for(Item item : items) {
            if(item.type().equalsIgnoreCase("spær")) {
                lengths.add(item.length());
            }
        }
        Collections.sort(lengths);
        return lengths;
    }

    public List<Item> calculateSpær(Carport carport, double lowestSpærLength) {
        Math.ceil(45);
        double carportWidthCM = carport.getWidth()*100;
        double carportLengthCM = carport.getLength()*100;
        //Calculate amount
        int spærAmount = calculateAmountSpær(carportLengthCM);

        //Calculate length
        if(carportWidthCM < lowestSpærLength) {
            //for at finde den rigtige længde: check
        }
        //calculate lengths

        //maybe recalculate amount

        /*
        Seudocode
        CalculateSpær {
            If under 360(lowest spær width)
            SpærLength=(spær der er tættest på)carport width/2
            SpærAmount= SpærAmount /2
            Else if over max
            Else
            Spær amount==
            SpærLength=carport width
        */

        return null;
    }

    public int calculateAmountSpær(double carportLengthCM) {
        return (int)Math.ceil(carportLengthCM/60+2);
    }
}

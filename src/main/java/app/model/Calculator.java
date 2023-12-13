package app.model;

import app.controllers.ItemController;
import app.exceptions.DatabaseException;
import app.exceptions.DimensionException;
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
     *
     * @return this will always return either a new or an existing instance of Calculator
     */
    public static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }
        instance.items = ItemController.getAllItems();
        return instance;
    }

    /**
     * Collects itemList data from several methods
     *
     * @param carport
     * @return ItemList with all collected data
     */
    public ItemList calculateItemList(Carport carport) {
        ArrayList<Integer> spærLengths = getSpærLengths();
        int lowestSpærLength = spærLengths.get(0);
        int highestSpærLength = spærLengths.get(spærLengths.size());
        try {
            List<Item> spær = calculateSpær(carport, lowestSpærLength, highestSpærLength, spærLengths);
        } catch (Exception e) {
            e.getMessage();
        }

        return null;
    }

    /**
     * Get "spær" lengths in a list and sort
     *
     * @return
     */
    private ArrayList<Integer> getSpærLengths() {
        ArrayList<Integer> lengths = new ArrayList<>();
        for (Item item : items) {
            if (item.type().equalsIgnoreCase("spær")) {
                lengths.add((int) item.length());
            }
        }
        Collections.sort(lengths);
        return lengths;
    }

    private List<Item> getOnlySpær() {
        List<Item> items = new ArrayList<>();
        for (Item item : items) {
            if (item.type().equalsIgnoreCase("spær")) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * @return Will return the first element matching the input length, otherwise returns null
     */
    private Item getSpærByLength(int length) {
        List<Item> items = new ArrayList<>();
        for (Item item : items) {
            if (item.length() == length) {
                return item;
            }
        }
        return null;
    }

    /**
     * Vi kan gå ud fra at bredde*2 altid vil være lig en spær
     * Der skal ikke kunne eksistere spær der ikke kan deles med 60
     *
     * @param carport
     * @param lowestSpærLength
     * @param highestSpærLength
     * @return
     */
    private List<Item> calculateSpær(Carport carport, int lowestSpærLength, int highestSpærLength, ArrayList<Integer> spærLengths) throws DimensionException {
        //VERIFY CORRECT DIMENSIONS
        if (highestSpærLength < carport.getWidth()) {
            throw new DimensionException("Carport is too wide for available 'spær', please add new Item or remove options for carport-width");
        }

        //Setup
        int carportWidthCM = (int) carport.getWidth() * 100;
        int carportLengthCM = (int) carport.getLength() * 100;
        List<Item> spærToAdd = new ArrayList<>();
        List<Item> spærFromItems = getOnlySpær();

        //Hjælpemetoder
        Boolean isWidthSmallerThatSpær = carportWidthCM < lowestSpærLength;
        boolean isLongPieceMoreEfficient = carportWidthCM - lowestSpærLength > findClosestHigherNumberInList(spærLengths, carportWidthCM * 2) - carportWidthCM * 2;
        boolean IsDoubleWidthTooLong = carportWidthCM * 2 < highestSpærLength;

        //Set sets amount and length of spær. Length is set to the one the one thats closest in length(as longs as it's longer)
        int spærAmount = calculateAmountSpær(carportLengthCM);
        int spærLength = findClosestHigherNumberInList(spærLengths, carportWidthCM);

        //Check if we should rather use half the amount of longer pieces
        if (isWidthSmallerThatSpær && isLongPieceMoreEfficient && IsDoubleWidthTooLong) {
            spærLength = findClosestHigherNumberInList(spærLengths, carportWidthCM * 2);
            spærAmount = spærAmount / 2;
            //check if amount is even and add a short piece for the remaining spær
            if (spærAmount % 2 != 0) {
                Item item = getSpærByLength(findClosestHigherNumberInList(spærLengths, carportWidthCM));
                item.setQuantity(1); //????????????????????????????????????????????????????????
                spærToAdd.add(item);
            }
        }

        //add spær and return
        Item item = getSpærByLength(spærLength);
        item.setQuantity(spærAmount);
        spærToAdd.add(item);
        return spærToAdd;
    }

    public int calculateAmountSpær(double carportLengthCM) {
        return (int) Math.ceil(carportLengthCM / 60 + 2);
    }

    private int findClosestHigherNumberInList(ArrayList<Integer> numbers, int n) {
        // Initialize variables to keep track of the minimum difference and the closest higher number
        int minDifference = Integer.MAX_VALUE;
        int closestHigherNumber = Integer.MAX_VALUE;

        // Iterate through the sorted list
        for (int number : numbers) {
            // Calculate the difference between the current number and 'n'
            int difference = number - n;

            // Check if the current number is higher than 'n' and has a smaller difference
            if (difference > 0 && difference < minDifference) {
                minDifference = difference;
                closestHigherNumber = number;
            }
        }
        return closestHigherNumber;
    }
}

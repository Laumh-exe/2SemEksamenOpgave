package app.model;

import app.controllers.ItemController;
import app.exceptions.DatabaseException;
import app.exceptions.DimensionException;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.persistence.ConnectionPool;
import app.persistence.ItemMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.round;

/**
 * This is a Singelton
 */
public class Calculator {

    private List<Item> items;
    private int spærQuantity;
    private int stolpeQuantity;
    private int remQuantity;

    private static Calculator instance = null;

    /**
     * Setup of Singleton
     * @param connectionPool
     *
     * @return this will always return either a new or an existing instance of Calculator
     */
    public static Calculator getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            instance = new Calculator();
        }
        instance.spærQuantity = 0;
        instance.stolpeQuantity = 0;
        instance.remQuantity = 0;
        try {
            instance.items = ItemController.getAllItems(connectionPool);
        }
        catch (SQLException e){
            e.getMessage();
        }
        return instance;
    }

    public static Calculator getInstance() {
        return instance;
    }

    /**
     * Collects itemList data from several methods
     *
     * @param carport
     * @return ItemList with all collected data
     */
    public ItemList calculateItemList(Carport carport) {
        ArrayList<Item> itemList = new ArrayList<>();
        ArrayList<Item> spær = new ArrayList<>();
        try {
            spær = calculateSpær(carport);
        } catch (Exception e) {
            e.getMessage();
        }
        Item stolper = calculateStolper(carport);
        ArrayList<Item> rem = calculateRem(carport);
        itemList.addAll(rem);
        itemList.add(stolper);
        itemList.addAll(spær);
        return null;
    }

    private Item calculateStolper(Carport carport) {
        int carportLengthCM = (int) carport.getLength() * 100;
        int shedWidthCM = (int) carport.getShed().getWidth() * 100;
        List<Item> stolper = items.stream().filter(a -> a.function() == "stolpe").collect(Collectors.toList());
        Item tmpStolpe = stolper.get(0);

        //Stolpe in middle of shed
        if(carport.isShed()) {
            if (shedWidthCM/250 > 1) {
                stolpeQuantity += shedWidthCM/250*2;
            }
            // Stolpe in mid-corners of shed
            stolpeQuantity += 2;
        }
        //Stolpe each corner
        stolpeQuantity += 4 + (carportLengthCM/250);

        return new Item(tmpStolpe.id(),tmpStolpe.price_pr_unit(),tmpStolpe.length(),tmpStolpe.unit(),tmpStolpe.description(),stolpeQuantity,tmpStolpe.function());
    }

    private ArrayList<Item> calculateRem(Carport carport) {
        //Hent rem istedet for spær

        ArrayList<Integer> remLengths = getRemLengths(); //USE STREAMS!
        ArrayList<Item> rem = new ArrayList<>();
        int lowestRemLength = remLengths.get(0);
        int highestRemLength = remLengths.get(remLengths.size());
        int carportLengthCM = (int) carport.getLength() * 100;

        //If carport is smaller than the longest rem available - use rem closest to carportLength
        if(carportLengthCM < highestRemLength) {
            Item tmpItem = getRemByLength(findClosestHigherNumberInList(remLengths, carportLengthCM));
            rem.add(new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),2,tmpItem.function()));
            remQuantity += 2;
        }
        else {
            //Check each length from longest, to shortest
            for(int i = remLengths.size(); i >= 0; i--) {
                int remainingRemLength = carportLengthCM - remLengths.get(i) * 2;

                //If length of rem is the same as length of carport
                if (carportLengthCM == remLengths.get(i)) {
                    Item tmpItem = getRemByLength(remLengths.get(i));
                    rem.add(new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),2,tmpItem.function()));
                    remQuantity += 2;
                    break;
                }
                //If remaining length(each side combined) is between shortest and longest rem, add the rem longer...
                else if (remainingRemLength > lowestRemLength && remainingRemLength < highestRemLength) {
                    Item tmpItem = getRemByLength(findClosestHigherNumberInList(remLengths, remainingRemLength));
                    rem.add(new Item(tmpItem.id(), tmpItem.price_pr_unit(), tmpItem.length(), tmpItem.unit(), tmpItem.description(), 1, tmpItem.function()));
                    rem.add(new Item(tmpItem.id(), tmpItem.price_pr_unit(), remLengths.get(i), tmpItem.unit(), tmpItem.description(), 2, tmpItem.function()));
                    remQuantity += 3;
                    break;
                }
            }
        }
        return rem;
    }

    /**
     * Vi kan gå ud fra at bredde*2 altid vil være lig en spær
     * Der skal ikke kunne eksistere spær der ikke kan deles med 60
     *
     * @param carport
     * @return
     */
    private ArrayList<Item> calculateSpær(Carport carport) throws DimensionException {
        //Setup
        int carportWidthCM = (int) carport.getWidth() * 100;
        int carportLengthCM = (int) carport.getLength() * 100;
        ArrayList<Item> spærToAdd = new ArrayList<>();
        List<Item> spærFromItems = items.stream().filter(a -> a.description().equals("spær")).collect(Collectors.toList());
        ArrayList<Integer> spærLengths = getSpærLengths();
        int lowestSpærLength = spærLengths.get(0);
        int highestSpærLength = spærLengths.get(spærLengths.size());

        //VERIFY CORRECT DIMENSIONS
        if (highestSpærLength < carport.getWidth()) {
            throw new DimensionException("Carport is too wide for available 'spær', please add new Item or remove options for carport-width");
        }

        //Hjælpemetoder
        Boolean isWidthSmallerThatSpær = carportWidthCM < lowestSpærLength;
        boolean isLongPieceMoreEfficient = carportWidthCM - lowestSpærLength > findClosestHigherNumberInList(spærLengths, carportWidthCM * 2) - carportWidthCM * 2;
        boolean IsDoubleWidthTooLong = carportWidthCM * 2 < highestSpærLength;

        //Set sets amount and length of spær. Length is set to the one the one thats closest in length(as longs as it's longer)
        int spærAmount = getSpærQuantaty(carportLengthCM);
        int spærLength = findClosestHigherNumberInList(spærLengths, carportWidthCM);

        //Check if we should rather use half the amount of longer pieces
        if (isWidthSmallerThatSpær && isLongPieceMoreEfficient && IsDoubleWidthTooLong) {
            spærLength = findClosestHigherNumberInList(spærLengths, carportWidthCM * 2);
            spærAmount = spærAmount / 2;
            spærQuantity = spærAmount;
            //check if amount is even and add a short piece for the remaining spær
            if (spærAmount % 2 != 0) {
                Item tmpItem = getSpærByLength(findClosestHigherNumberInList(spærLengths, carportWidthCM));
                Item item = new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),1,tmpItem.function());
                spærToAdd.add(item);
                spærQuantity++;
            }
        }

        //add spær and return
        Item tmpItem = getSpærByLength(spærLength);
        Item item = new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),spærAmount,tmpItem.function());
        spærToAdd.add(item);
        return spærToAdd;
    }

    public int getSpærQuantaty(double carportLengthCM) {
        return (int) Math.ceil(carportLengthCM / 60 + 2);
    }

    public int getSpærQuantity() {
        return spærQuantity;
    }

    public int getStolpeQuantity() {
        return stolpeQuantity;
    }

    public int getRemQuantity() {
        return remQuantity;
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


    /**
     * Get "spær" lengths in a list and sort
     *
     * @return
     */
    private ArrayList<Integer> getSpærLengths() {
        ArrayList<Integer> lengths = new ArrayList<>();
        for (Item item : items) {
            if (item.function().equalsIgnoreCase("spær")) {
                lengths.add((int) item.length());
            }
        }
        Collections.sort(lengths);
        return lengths;
    }
    /**
     * Get "rem" lengths in a list and sort
     *
     * @return
     */
    private ArrayList<Integer> getRemLengths() {
        ArrayList<Integer> lengths = new ArrayList<>();
        for (Item item : items) {
            if (item.function().equalsIgnoreCase("rem")) {
                lengths.add((int) item.length());
            }
        }
        Collections.sort(lengths);
        return lengths;
    }

    /**
     * @return Will return the first element matching the input length, otherwise returns null
     */
    private Item getSpærByLength(int length) {
        List<Item> spærList = items.stream().filter(a -> a.description().equals("spær")).collect(Collectors.toList());
        for (Item item : spærList) {
            if (item.length() == length) {
                return item;
            }
        }
        return null;
    }
    /**
     * @return Will return the first element matching the input length, otherwise returns null
     */
    private Item getRemByLength(int length) {
        List<Item> remList = items.stream().filter(a -> a.description().equals("rem")).collect(Collectors.toList());
        for (Item item : remList) {
            if (item.length() == length) {
                return item;
            }
        }
        return null;
    }
}

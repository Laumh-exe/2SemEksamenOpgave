package app.model;

import app.controllers.ItemController;
import app.exceptions.DimensionException;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.persistence.ConnectionPool;

import java.sql.SQLException;
import java.util.ArrayList;
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
    private int stolpeShedQuantity;
    private int remQuantity;

    private static Calculator instance = null;


    /**
     * Setup of Singleton
     * @param connectionPool
     * @return this will always return either a new or an existing instance of Calculator
     */
    public static Calculator getInstance(ConnectionPool connectionPool) {
        if (instance == null) {
            instance = new Calculator();
        }
        instance.spærQuantity = 0;
        instance.stolpeQuantity = 0;
        instance.remQuantity = 0;
        instance.stolpeShedQuantity = 0;
        try {
            instance.items = ItemController.getAllItems(connectionPool);
        }
        catch (SQLException e){
            e.getMessage();
        }
        return instance;
    }

    public static Calculator getInstance() {
        instance.spærQuantity = 0;
        instance.stolpeQuantity = 0;
        instance.remQuantity = 0;
        instance.stolpeShedQuantity = 0;
        return instance;
    }

    public int getStolpeShedQuantity() {
        return stolpeShedQuantity;
    }

    /**
     * Collects itemList data from several methods
     *
     * @param carport
     * @return ItemList with all collected data
     */
    public ItemList calculateItemList(Carport carport) {
        ItemList itemList = new ItemList();
        List<Item> spær = new ArrayList<>();
        int carportLengthCM = (int) (carport.getLengthMeter() * 100);
        int carportWidthCM = (int) carport.getWidthMeter() * 100;
        int shedWidthCM = carport.hasShed() ? (int) (carport.getShed().getWidthMeter() * 100) : 0;

        try {
            spær = calculateSpær(carport, carportLengthCM, carportWidthCM);
        } catch (Exception e) {
            e.getMessage();

        }
        Item stolper = calculateStolper(carport, shedWidthCM, carportLengthCM);
        List<Item> rem = calculateRem(carportLengthCM);
        itemList.addAll(rem);
        itemList.add(stolper);
        itemList.addAll(spær);
        return itemList;
    }

    public Item calculateStolper(Carport carport, int shedWidthCM, int carportLengthCM) {
        List<Item> stolper = items.stream().filter(a -> a.function().equalsIgnoreCase("stolpe")).collect(Collectors.toList());
        Item tmpStolpe = stolper.get(0);

        //Stolpe in middle of shed
        if(carport.hasShed()) {
            if (shedWidthCM/250 > 1) {
                //Devide by 250(remove one for the cause we already have a corner further down)
                stolpeQuantity += (shedWidthCM/250-1)*2;
                stolpeShedQuantity += (shedWidthCM/250-1)*2;
            }
            // Stolpe in mid-corners of shed
            stolpeQuantity += 2;
            stolpeShedQuantity += 2;
        }
        //Stolpe each corner(remove one for the cause we already hae a corner)
        stolpeQuantity += 4 + (carportLengthCM/250)-1;

        return new Item(tmpStolpe.id(),tmpStolpe.price_pr_unit(),tmpStolpe.length(),tmpStolpe.unit(),tmpStolpe.description(),stolpeQuantity,tmpStolpe.function());
    }

    public List<Item> calculateRem(int carportLengthCM) {
        List<Integer> remLengths = getRemLengths();
        ArrayList<Item> rem = new ArrayList<>();
        int lowestRemLength = remLengths.get(0);
        int highestRemLength = remLengths.get(remLengths.size()-1);
        int remLength = 0;

        //If carport is smaller than the longest rem available - use rem closest to carportLength
        if(carportLengthCM < highestRemLength) {
            Item tmpItem = getRemByLength(findClosestHigherNumberInList(remLengths, carportLengthCM));
            rem.add(new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),2,tmpItem.function()));
            remQuantity += 2;
        }
        else {
            //Check each length from longest, to shortest
            for(int i = remLengths.size()-1; i >= 0; i--) {
                int currentRem = remLengths.get(i);
                int remainingRemLength = (carportLengthCM - currentRem) * 2;

                //If length of rem is the same as length of carport
                if (carportLengthCM == remLengths.get(i)) {
                    Item tmpItem = getRemByLength(remLengths.get(i));
                    rem.add(new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),2,tmpItem.function()));
                    remQuantity += 2;
                    break;
                }
                //If remaining length(each side combined) is between shortest and longest rem, add the rem longer...
                else if (remainingRemLength >= lowestRemLength && remainingRemLength <= highestRemLength) {
                    if (compareToLengths(remLengths, remainingRemLength)) {
                        remLength = remainingRemLength;
                    }
                    else {
                        remLength = findClosestHigherNumberInList(remLengths, remainingRemLength);
                    }
                    Item tmpItem = getRemByLength(remLength);
                    rem.add(new Item(tmpItem.id(), tmpItem.price_pr_unit(), tmpItem.length(), tmpItem.unit(), tmpItem.description(), 1, tmpItem.function()));
                    tmpItem = getRemByLength(currentRem);
                    rem.add(new Item(tmpItem.id(), tmpItem.price_pr_unit(), tmpItem.length(), tmpItem.unit(), tmpItem.description(), 2, tmpItem.function()));
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

    public List<Item> calculateSpær(Carport carport, int carportLengthCM, int carportWidthCM) throws DimensionException {
        //Setup
        ArrayList<Item> spærToAdd = new ArrayList<>();
        List<Item> spærFromItems = items.stream().filter(a -> a.function().equals("spær")).collect(Collectors.toList());
        List<Integer> spærLengths = getSpærLengths();

        int lowestSpærLength = spærLengths.get(0);
        int highestSpærLength = spærLengths.get(spærLengths.size()-1);
        int spærLength = 0;

        //VERIFY CORRECT DIMENSIONS
        if (highestSpærLength < carport.getWidthMeter()) {
            throw new DimensionException("Carport is too wide for available 'spær', please add new Item or remove options for carport-width");
        }

        //Hjælpemetoder
        Boolean isWidthSmallerThatSpær = carportWidthCM < lowestSpærLength;
        boolean isLongPieceMoreEfficient = carportWidthCM - lowestSpærLength > findClosestHigherNumberInList(spærLengths, carportWidthCM * 2) - carportWidthCM * 2;
        boolean IsDoubleWidthTooLong = carportWidthCM * 2 < highestSpærLength;

        //Set sets amount and length of spær. Length is set to the one the one thats closest in length(as longs as it's longer)
        spærQuantity = calculateAmountSpær(carportLengthCM);
        //Check to see if carportWidth is the same as a spær
        if (compareToLengths(spærLengths, carportWidthCM)) {
            spærLength = carportWidthCM;
        }
        else {
            spærLength = findClosestHigherNumberInList(spærLengths, carportWidthCM);
        }

        //Check if we should rather use half the amount of longer pieces
        if (isWidthSmallerThatSpær && isLongPieceMoreEfficient && IsDoubleWidthTooLong) {
            spærLength = findClosestHigherNumberInList(spærLengths, carportWidthCM * 2);
            spærQuantity = spærQuantity / 2;
            //check if amount is even and add a short piece for the remaining spær
            if (spærQuantity % 2 != 0) {
                Item tmpItem = getSpærByLength(findClosestHigherNumberInList(spærLengths, carportWidthCM));
                Item item = new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),1,tmpItem.function());
                spærToAdd.add(item);
                spærQuantity++;
            }
        }
        //add spær and return
        Item tmpItem = getSpærByLength(spærLength);
        Item item = new Item(tmpItem.id(),tmpItem.price_pr_unit(),tmpItem.length(),tmpItem.unit(),tmpItem.description(),spærQuantity,tmpItem.function());
        spærToAdd.add(item);
        return spærToAdd;
    }

    private boolean compareToLengths(List<Integer> lengths, int n) {
        for(Integer length : lengths) {
            if(length==n) {
                return true;
            }
        }
        return false;
    }

    public int calculateAmountSpær(double carportLengthCM) {
        return (int) Math.ceil(carportLengthCM / 60f + 2);
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



    private int findClosestHigherNumberInList(List<Integer> numbers, int n) {

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

    private List<Integer> getSpærLengths() {

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

    private List<Integer> getRemLengths() {
        List<Integer> lengths = new ArrayList<>();

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
        List<Item> spærList = items.stream().filter(a -> a.function().equals("spær")).collect(Collectors.toList());
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
        List<Item> remList = items.stream().filter(a -> a.function().equals("rem")).collect(Collectors.toList());
        for (Item item : remList) {
            if (item.length() == length) {
                return item;
            }
        }
        return null;
    }

}


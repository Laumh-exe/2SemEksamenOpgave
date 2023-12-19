package app.controllers;

import app.model.Calculator;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import app.model.entities.Item;

import java.util.List;

import java.util.List;

public class CarportController {
    public static Carport createCarport(String hasShed, Context ctx, ConnectionPool connectionPool) {

        double length = Double.parseDouble(ctx.formParam("længde"));
        double width = Double.parseDouble(ctx.formParam("bredde"));

        Carport carportWithoutItemList = new Carport(length, width);
        Carport carportWithItemlist = null;

        Calculator calculator = Calculator.getInstance(connectionPool);

        if (hasShed != null) {

            double shedLength = Double.parseDouble(ctx.formParam("skur-Længde"));
            double shedWidth = Double.parseDouble(ctx.formParam("skur-Bredde"));
            Shed shed = new Shed(shedLength, shedWidth);

            carportWithoutItemList = new Carport(length, width, shed);

            ItemList itemlist = calculator.calculateItemList(carportWithoutItemList);

            carportWithItemlist = new Carport(carportWithoutItemList.getLengthMeter(), carportWithoutItemList.getWidthMeter(),
                    carportWithoutItemList.getShed(), itemlist);


            return carportWithItemlist;
        }

        ItemList itemlist = calculator.calculateItemList(carportWithoutItemList);

        carportWithItemlist = new Carport(carportWithoutItemList.getLengthMeter(), carportWithoutItemList.getWidthMeter(), itemlist);

        return carportWithItemlist;
    }


    public static double getPrice(Carport carport) {
        List<Item> itemList = carport.getItemList().getItemList();
        double total = 0;

        for (Item item : itemList) {
            total += item.price_pr_unit() * item.quantity();
        }

        return total;
    }
}


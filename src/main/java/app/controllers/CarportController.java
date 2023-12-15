package app.controllers;

import app.model.Calculator;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.List;

public class CarportController {
    public static Carport createCarport(Context ctx, ConnectionPool connectionPool) {

        double length = Double.parseDouble(ctx.formParam("længde"));
        double width = Double.parseDouble(ctx.formParam("bredde"));

        String isShed = ctx.formParam("skur");

        if (isShed != null) {

            double shedLength = Double.parseDouble(ctx.formParam("skur-Længde"));
            double shedWidth = Double.parseDouble(ctx.formParam("skur-Bredde"));

            Shed shed = new Shed(shedLength, shedWidth);

            Carport carportWithoutItemList = new Carport(length, width, shed);

            ItemList itemlist = Calculator.calculateItemList(carportWithoutItemList);

            Carport carportWithItemlist = new Carport(carportWithoutItemList.getLength(), carportWithoutItemList.getWidth(),
                    carportWithoutItemList.getShed(), itemlist);

            return carportWithItemlist;
        }
        return new Carport(length, width);
    }

    public static double getPrice(Carport carport, ConnectionPool connectionPool) {
        List<Item> itemList = carport.getItemList().getItemList();
        double total = 0;

        for (Item item : itemList) {
            total += item.price_pr_unit() * item.quantity();
        }

        return total;
    }

}


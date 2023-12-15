package app.controllers;

import app.model.Calculator;
import app.model.entities.Carport;
import app.model.entities.ItemList;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class CarportController {
    public static Carport createCarport(Context ctx, ConnectionPool connectionPool) {

        double length = Double.parseDouble(ctx.formParam("længde"));
        double width = Double.parseDouble(ctx.formParam("bredde"));

        Carport carportWithoutItemList = new Carport(length, width);

        String isShed = ctx.formParam("skur");

        if (isShed != null) {

            double shedLength = Double.parseDouble(ctx.formParam("skur-Længde"));
            double shedWidth = Double.parseDouble(ctx.formParam("skur-Bredde"));
            Shed shed = new Shed(shedLength, shedWidth);


            carportWithoutItemList = new Carport(length, width, shed);

            ItemList itemlistWithShed = Calculator.calculateItemList(carportWithoutItemList);

            Carport carportWithShedAndItemlist = new Carport(carportWithoutItemList.getLength(), carportWithoutItemList.getWidth(),
                    carportWithoutItemList.getShed(), itemlistWithShed);

            return carportWithShedAndItemlist;
        }

        ItemList itemlist = Calculator.calculateItemList(carportWithoutItemList);

        Carport carportWithItemlist = new Carport(carportWithoutItemList.getLength(), carportWithoutItemList.getWidth(), itemlist);

        return carportWithItemlist;
    }
}

package app.controllers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.model.Calculator;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

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
        return new Carport(length,width);
    }

    public static void show2dDrawing(Context ctx){
        Order order = ctx.sessionAttribute("order");
        Carport carport = order.getCarport();
        spærDrawing(ctx, carport);
        stolbeDrawing(ctx, carport);
        
        
        
    }

    private static void stolbeDrawing(Context ctx, Carport carport) {
        List<Item> stolbeList = carport.getItemList().getItemList().stream().filter(a -> a.description() == "stolbe").collect(Collectors.toList());
        ctx.sessionAttribute("stolbeSpasing", 60); //TODO: get the actual spasing form Lauritz
        ctx.sessionAttribute("stolber", stolbeList);
    }

    private static void spærDrawing(Context ctx, Carport carport) {
        List<Item> spærList = carport.getItemList().getItemList().stream().filter(a -> a.description() == "spær").collect(Collectors.toList());
        ctx.sessionAttribute("spærSpasing", 60); //TODO: get the actual spasing form Lauritz
        ctx.sessionAttribute("spær", spærList);
    }
}

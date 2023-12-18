package app.controllers;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import app.model.Calculator;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import app.services.CarportSVG;
import io.javalin.http.Context;

public class CarportController {
    public static Carport createCarport(Context ctx, ConnectionPool connectionPool) {

        double length = Double.parseDouble(ctx.formParam("længde"));
        double width = Double.parseDouble(ctx.formParam("bredde"));
        Calculator calculator = Calculator.getInstance(connectionPool);

        String isShed = ctx.formParam("skur");

        if (isShed != null) {

            double shedLength = Double.parseDouble(ctx.formParam("skur-Længde"));
            double shedWidth = Double.parseDouble(ctx.formParam("skur-Bredde"));

            Shed shed = new Shed(shedLength, shedWidth);

            Carport carportWithoutItemList = new Carport(length, width, shed);

            ItemList itemlist = calculator.calculateItemList(carportWithoutItemList);

            Carport carportWithItemlist = new Carport(carportWithoutItemList.getLengthMeter(), carportWithoutItemList.getWidthMeter(),
                    carportWithoutItemList.getShed(), itemlist);

            return carportWithItemlist;
        }
        return new Carport(length,width);
    }

    public static void show2dDrawing(Context ctx, ConnectionPool connectionPool){
        Locale.setDefault(new Locale("US"));
        Order order = ctx.sessionAttribute("order");
        Carport carport = order.getCarport();
        Calculator.getInstance(connectionPool); // this is to make sure that calculator is insansed elsewhere
    
        CarportSVG svg = new CarportSVG(carport);
        
        ctx.sessionAttribute("svg", svg.toString());
        ctx.render("VisuliseCarport.html");
    }
}

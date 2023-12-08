package app.controllers;

import app.model.entities.Carport;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class CarportController {
    public static Carport createCarport(Context ctx, ConnectionPool connectionPool) {
        double length = Double.parseDouble(ctx.formParam("længde"));
        double width = Double.parseDouble(ctx.formParam("bredde"));
        String isShed = ctx.formParam("skur");
        if (isShed == null) {
            double shedLength = Double.parseDouble(ctx.formParam("skur-Længde"));
            double shedWidth = Double.parseDouble(ctx.formParam("skur-Bredde"));
            Shed shed = new Shed(shedLength, shedWidth);
            Carport carport = new Carport(length, width, shed);
            return carport;
        }
        return new Carport(length,width);
    }
}

package app.controllers;

import app.entities.Carport;
import app.entities.Order;
import app.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class CarportController {
    public void createCarport(Context ctx, ConnectionPool connectionPool) {
        float length = Float.parseFloat(ctx.formParam("længde"));
        float width = Float.parseFloat(ctx.formParam("bredde"));
        float shedLength = Float.parseFloat(ctx.formParam("skurlængde"));
        float shedWidth = Float.parseFloat(ctx.formParam("skurBredde"));
        //Add more options if needed

        Shed shed = new Shed(shedLength, shedWidth);
        Carport carport = new Carport(length, width, shed);
        ctx.sessionAttribute("carport", carport);

        // send til login side hvis bruger ikke er logget ind - ellers send til odreside
        if (ctx.formParam("currentUser") != null) {
            ctx.render("/confirmOrders.html");
        } else {
            ctx.render("/login.html");
        }
    }
}

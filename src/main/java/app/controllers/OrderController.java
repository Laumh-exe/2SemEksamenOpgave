package app.controllers;

import app.entities.Order;
import app.entities.User;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class OrderController {

    public static void placeOrder(Context ctx, ConnectionPool connectionPool) {

        Order orderToPlace = ctx.sessionAttribute("newOrder");

        User user = ctx.sessionAttribute("currentUser");




    }
}

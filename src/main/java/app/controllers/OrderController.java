package app.controllers;

import java.util.Date;
import java.util.List;

import app.entities.Carport;
import app.entities.Order;
import app.entities.OrderStatus;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Authentication;

import static app.entities.OrderStatus.ORDER_NOT_ACCEPTED;

public class OrderController {
    public static List<Order> seeAllOrders(ConnectionPool connectionPool) {
        return null;
    }

    public static void createOrder(Context ctx, ConnectionPool connectionPool) {
        // hent carport og lav ordre!
        Carport carport = ctx.sessionAttribute("carport");
        Authentication.User currentUser = ctx.sessionAttribute("currentUser");

        //Create order
        Date date = new Date(System.currentTimeMillis());
        Order order = new Order(date, ORDER_NOT_ACCEPTED, carport);

        ctx.sessionAttribute("order", order);

        // send til login side hvis bruger ikke er logget ind - ellers send til odreside
        if (ctx.formParam("currentUser") != null) {
            ctx.render("/confirmOrders.html");
        } else {
            ctx.render("/login.html");
        }

    }

}

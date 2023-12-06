package app.controllers;

import java.util.Date;
import java.util.List;

import app.entities.Carport;
import app.entities.Order;
import app.entities.OrderStatus;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Authentication;

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
        Order order = new Order(Date date, OrderStatus status, double price, double carportLength, double carportWidth, double shedLength, double shedWidth)

    }

}

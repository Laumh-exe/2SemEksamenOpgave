package app.controllers;

import java.util.List;


import app.entities.Order;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class OrderController {
    public static void sellerSeeAllOrders(Context ctx, ConnectionPool connectionPool){
        List<Order> allOrders = OrderMapper.getAllOrders(connectionPool);
        ctx.sessionAttribute("allOrders", allOrders);
        ctx.render("SellersAllOrders.html");
    }
}

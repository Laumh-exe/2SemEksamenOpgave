package app.controllers;

import java.sql.SQLException;
import java.util.List;

import app.entities.Order;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;

import io.javalin.http.Context;

public class OrderController {
    
    public static void sellerSeeAllOrders(Context ctx, ConnectionPool connectionPool){
        List<Order> allOrders = null;
        try {
            allOrders = OrderMapper.getAllOrders(connectionPool);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ctx.sessionAttribute("allOrders", allOrders);
        ctx.render("SellersAllOrders.html");
    }
    
    public static void placeOrder(Context ctx, ConnectionPool connectionPool) {

        Order orderToPlace = ctx.sessionAttribute("newOrder");

        User user = ctx.sessionAttribute("currentUser");


    }
}

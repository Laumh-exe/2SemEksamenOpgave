package app.controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import app.entities.Carport;
import app.entities.Customer;
import app.entities.Order;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;

import io.javalin.http.Context;
import org.eclipse.jetty.server.Authentication;

import static app.entities.OrderStatus.ORDER_NOT_ACCEPTED;

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

    public static void createOrder(Context ctx, ConnectionPool connectionPool) {
  
        // hent carport og lav ordre!
        Carport carport = CarportController.createCarport(ctx, connectionPool);
        Customer currentUser = ctx.sessionAttribute("currentUser");

        //Create order
        Date date = new Date(System.currentTimeMillis());
        Order order = new Order(date, ORDER_NOT_ACCEPTED, carport);
        ctx.sessionAttribute("order", order);

        // send til login side hvis bruger ikke er logget ind - ellers send til odrreside
        if (currentUser != null) {

           // ctx.render("/confirmOrders.html");
     } else {
           // ctx.render("/login.html");
        }
    }
}

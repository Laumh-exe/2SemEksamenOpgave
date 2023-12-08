package app.controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import app.entities.Carport;
import app.entities.Customer;
import app.entities.Order;
import app.entities.OrderStatus;
import app.entities.Salesperson;
import app.entities.User;
import app.exceptions.OrderNotFoundException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
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
    public static void setupUpdatePage(Context ctx, ConnectionPool connectionPool){
        List<Salesperson> salespeople = null;
        try {   
            salespeople = UserMapper.getAllSellerID(connectionPool);
        } catch (SQLException e) {
            ctx.sessionAttribute("errorMessage", "Database not responding");
        }
        ctx.sessionAttribute("allSalespersonId", salespeople);
        ctx.render("updateOrder.html");
    }

    public static void updateOrderWidthOutShed(Context ctx, ConnectionPool connectionPool){
        int salespersonId = Integer.parseInt(ctx.formParam("salespersonId"));
        double newCarportLength = Double.parseDouble(ctx.formParam("newCarprotLength"));
        double newCarportWidth = Double.parseDouble(ctx.formParam("newCarprotWidth"));
        OrderStatus newStatus = OrderStatus.valueOf(ctx.formParam("newStatus"));

        Order order = ctx.sessionAttribute("order");
        order.setSalesPersonId(salespersonId);
        order.setCarportLength(newCarportLength);
        order.setCarportWidth(newCarportWidth);
        order.setStatus(newStatus);
        
        try {
            OrderMapper.updateOrderWidthOutShed(order, connectionPool);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OrderNotFoundException e) {
            ctx.sessionAttribute("errorMessage", "Order did not exist");
        }

        ctx.render("updateOrder.html");

    }
}

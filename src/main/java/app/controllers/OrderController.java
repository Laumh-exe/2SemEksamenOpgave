package app.controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import app.model.entities.Carport;
import app.model.entities.Customer;
import app.model.entities.Order;
import app.model.entities.OrderStatus;
import app.model.entities.Salesperson;
import app.model.entities.User;
import app.exceptions.OrderNotFoundException;

import app.exceptions.DatabaseException;

import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import static app.model.entities.OrderStatus.ORDER_NOT_ACCEPTED;

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


        Order orderToPlace = ctx.sessionAttribute("order");

        
        orderToPlace.setStatus(OrderStatus.CUSTOMER_ACCEPTED);
        
        User user = ctx.sessionAttribute("currentUser");
        if (orderToPlace.getCustomerId() == -1){
            orderToPlace.setCustomerId(user.getId());
        }
        
 
        try{
            OrderMapper.placeOrder(user, orderToPlace, connectionPool);
            ctx.render("/offerRequestConfirmed.html");
        }
        catch (DatabaseException e){

            ctx.attribute("dbConnectionError", e);
            ctx.render("/confirmOfferRequest.html");
        }
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
            ctx.redirect("/confirmOffer");
     } else {
            ctx.redirect("/login");
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

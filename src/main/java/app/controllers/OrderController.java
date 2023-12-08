package app.controllers;

import java.util.Date;
import java.util.List;

import app.entities.*;

import app.exceptions.DatabaseException;


import java.sql.SQLException;

import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Authentication;

import static app.entities.OrderStatus.ORDER_NOT_ACCEPTED;

public class OrderController {

    public static void sellerSeeAllOrders(Context ctx, ConnectionPool connectionPool) throws SQLException {

        List<Order> allOrders = OrderMapper.getAllOrders(connectionPool);
        ctx.sessionAttribute("allOrders", allOrders);
        ctx.render("SellersAllOrders.html");
    }


    public static void placeOrder(Context ctx, ConnectionPool connectionPool) {


        Order orderToPlace = ctx.sessionAttribute("order");



        orderToPlace.setStatus(OrderStatus.CUSTOMER_ACCEPTED);

        System.out.println(orderToPlace);

        User user = ctx.sessionAttribute("currentUser");
      
 
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

        //TODO: Better solution to checking if someone is logged in
        User testUser = new Customer(1, "customer", "customer", "customer@email.com", "customer", "customer", 200);
        ctx.sessionAttribute("currentUser", testUser);

        Customer currentUser = ctx.sessionAttribute("currentUser");

        //Create order
        Date date = new Date(System.currentTimeMillis());
        Order order = new Order(date, ORDER_NOT_ACCEPTED, carport);
        ctx.sessionAttribute("order", order);

        // send til login side hvis bruger ikke er logget ind - ellers send til odrreside
        if (currentUser != null) {

           ctx.render("/confirmOfferRequest.html");
     } else {
            ctx.render("/login.html");

        }
    }
}

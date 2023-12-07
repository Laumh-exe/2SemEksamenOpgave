package app.controllers;

import java.util.Date;
import java.util.List;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;

import io.javalin.http.Context;

public class OrderController {
    
    public static void sellerSeeAllOrders(Context ctx, ConnectionPool connectionPool){
        List<Order> allOrders = OrderMapper.getAllOrders(connectionPool);
        ctx.sessionAttribute("allOrders", allOrders);
        ctx.render("SellersAllOrders.html");
    }
    
    public static void placeOrderInDB(Context ctx, ConnectionPool connectionPool) {



        //Order orderToPlace = ctx.sessionAttribute("newOrder");

        // TODO: Delete this
        long millis = System.currentTimeMillis();
        Date dateOfOrder = new Date();
        Shed shed = new Shed(100, 100);
        Carport carport = new Carport(100, 200, shed);

        Order orderToPlace = new Order(1, 1, 1, dateOfOrder, OrderStatus.CUSTOMER_ACCEPTED,100,  carport);



        orderToPlace.setStatus(OrderStatus.CUSTOMER_ACCEPTED);

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
}

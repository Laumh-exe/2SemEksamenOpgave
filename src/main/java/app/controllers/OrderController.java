package app.controllers;

import java.util.List;

import app.entities.Order;
import app.entities.OrderStatus;
import app.entities.User;
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

        Order orderToPlace = ctx.sessionAttribute("newOrder");

        orderToPlace.setStatus(OrderStatus.CUSTOMER_ACCEPTED);

        User user = ctx.sessionAttribute("currentUser");


        try{
            OrderMapper.placeOrder(user, orderToPlace, connectionPool);
        }
        catch (DatabaseException e){

            ctx.attribute("dbConnectionError", e);
            ctx.render("/confirmOfferRequest.html");


        }







    }
}

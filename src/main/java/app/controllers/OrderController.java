package app.controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.model.entities.*;
import app.exceptions.OrderNotFoundException;

import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import static app.model.entities.OrderStatus.ORDER_NOT_ACCEPTED;

public class OrderController {

    public static void sellerSeeAllOrders(Context ctx, ConnectionPool connectionPool) {
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

    public static void placeOfferRequest(Context ctx, ConnectionPool connectionPool) {

        Order orderToPlace = ctx.sessionAttribute("order");

        orderToPlace.setStatus(OrderStatus.CUSTOMER_ACCEPTED);

        User user = ctx.sessionAttribute("currentUser");

        if (orderToPlace.getCustomerId() == -1) {
            orderToPlace.setCustomerId(user.getId());
        }

        try {
            OrderMapper.placeOrder(user, orderToPlace, connectionPool);

            ctx.render("/offerRequestConfirmed.html");
        } catch (SQLException e) {
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

        // send til login side hvis bruger ikke er logget ind - ellers send til ordreside
        if (currentUser != null) {
            ctx.redirect("/confirmOffer");
        } else {
            ctx.redirect("/login");
        }
    }

    public static void setupUpdatePage(Context ctx, ConnectionPool connectionPool) {
        List<Salesperson> salespeople = null;
        try {
            salespeople = UserMapper.getAllSellerID(connectionPool);
        } catch (SQLException e) {
            ctx.sessionAttribute("errorMessage", "Database not responding");
        }
        ctx.sessionAttribute("allSalespersonId", salespeople);
    }


    public static void updateOrderWidthOutShed(Context ctx, ConnectionPool connectionPool) {
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

    public static void customerSeeAllOrders(Context ctx, ConnectionPool connectionPool) {

        User customer = ctx.sessionAttribute("currentUser");

        List<Order> allOrders = null;
        try {
            allOrders = OrderMapper.getAllCustomersOrders(customer.getId(), connectionPool);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ctx.sessionAttribute("customersOrderlist", allOrders);
        ctx.render("/customersAllOrdersPage.html");
    }

    public static void customerSeeOrderDetails(Context ctx) {

        int orderID = Integer.parseInt(ctx.formParam("customerSeeOrderDetails"));

        List<Order> customersOrderlist = ctx.sessionAttribute("customersOrderlist");

        for (Order order : customersOrderlist) {
            if (order.getId() == orderID) {

                List<Item> itemList = order.getCarport().getItemList().getItemList();

                HashMap<Item, Double> pricePerQuantityOfItem = new HashMap<>();

                for (Item item : itemList) {
                    double price = item.price_pr_unit() * item.quantity();
                    pricePerQuantityOfItem.put(item, price);
                }

                Order orderToShowWithDetails = new Order(order.getId(), order.getCustomerId(), order.getSalespersonId(), order.getDate(), order.getStatus(), order.getPrice(), order.getCarport(), pricePerQuantityOfItem);

                ctx.sessionAttribute("orderToShow", orderToShowWithDetails);
            }
        }

        ctx.attribute("showPartsList", "show");

        ctx.render("/customersOrderDetails.html");

    }

    public static void showPartsList(Context ctx) {

        String showPartsList = ctx.formParam("showPartsList");

        if (showPartsList.equals("dontShow")) {
            ctx.attribute("showPartsList", "show");
        } else {
            ctx.attribute("showPartsList", "dontShow");
        }
        ctx.render("/customersOrderDetails.html");
    }

    public static void customerPaysForOrder(Context ctx, ConnectionPool connectionPool) {

        Order order = ctx.sessionAttribute("orderToShow");

        order.setStatus(OrderStatus.ORDER_PAID);

        try {
            OrderMapper.setStatusOfOrderInDB(order, connectionPool);
            ctx.attribute("showPartsList", "show");
            ctx.render("/customersOrderDetails.html");
        } catch (SQLException e) {

            ctx.attribute("sqlException", "Noget gik galt med betalingen");
            ctx.render("/customersOrderDetails.html");
        }
    }

    public static void salespersonTakeOrder(Context ctx, ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("salespersonTakeOrder"));
        User salesperson = ctx.sessionAttribute("currentUser");

        try {
            OrderMapper.salespersonTakeOrder(orderId, salesperson, connectionPool);
            sellerSeeAllOrders(ctx, connectionPool);
        } catch (SQLException e) {
            System.out.println("Something went wrong when asssigning salesperson to order");
        }
    }

    public static void salespersonUntakeOrder(Context ctx, ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("salespersonUntakeOrder"));
        User salesperson = ctx.sessionAttribute("currentUser");

        try {
            OrderMapper.salespersonUntakeOrder(orderId, salesperson, connectionPool);
            sellerSeeAllOrders(ctx, connectionPool);
        } catch (SQLException e) {
            System.out.println("Something went wrong when removing salesperson from order");
        }
    }

    public static void salespersonSeeAssignedOrders(Context ctx, ConnectionPool connectionPool) throws SQLException {

        User salesperson = ctx.sessionAttribute("currentUser");

        List<Order> allOrders = null;
        try {
            allOrders = OrderMapper.getAllSalespersonsOrders(salesperson.getId(), connectionPool);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ctx.sessionAttribute("salespersonOrderlist", allOrders);
        ctx.render("/salespersonSeeAssignedOrders.html");
    }

    public static void salespersonSeeOrderDetails(Context ctx, ConnectionPool connectionPool) {

        int orderID = Integer.parseInt(ctx.formParam("salespersonSeeOrderDetails"));

        List<Order> salespersonOrderlist = ctx.sessionAttribute("salespersonOrderlist");

        for (Order order : salespersonOrderlist) {
            if (order.getId() == orderID) {

                List<Item> itemList = order.getCarport().getItemList().getItemList();

                HashMap<Item, Double> pricePerQuantityOfItem = new HashMap<>();

                for (Item item : itemList) {
                    double price = item.price_pr_unit() * item.quantity();
                    pricePerQuantityOfItem.put(item, price);
                }

                Order orderToShowWithDetails = new Order(order.getId(), order.getCustomerId(), order.getSalespersonId(), order.getDate(), order.getStatus(), order.getPrice(), order.getCarport(), pricePerQuantityOfItem);

                ctx.sessionAttribute("orderToShow", orderToShowWithDetails);
            }
        }

        ctx.render("/salespersonOrderDetails.html");
    }

    public static void sendOffer(Context ctx, ConnectionPool connectionPool) throws SQLException{

        Order order = ctx.sessionAttribute("orderToShow");
        order.setStatus(OrderStatus.PRICE_PRESENTED);

        try {
            OrderMapper.setStatusOfOrderInDB(order, connectionPool);
            ctx.render("/salespersonOrderDetails.html");
        }
        catch (SQLException e){
            System.out.println("Something went wrong with sending offer");
        }


    }
}



package app.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
            OrderMapper.placeOrder(user.getId(), orderToPlace, connectionPool);
            ctx.render("/offerRequestConfirmed.html");

        } catch (SQLException e) {
            ctx.attribute("dbConnectionError", e);
            ctx.render("/confirmOfferRequest.html");
        }
    }

    public static void createOrder(Context ctx, ConnectionPool connectionPool) {

        // hent carport og lav ordre!
        String skur = ctx.formParam("skur");
        Carport carport = CarportController.createCarport(skur, ctx, connectionPool);

        Customer currentUser = ctx.sessionAttribute("currentUser");

        double price = CarportController.getPrice(carport);
        //Create order
        Date date = new Date(System.currentTimeMillis());

        Order order = new Order(date, ORDER_NOT_ACCEPTED, price ,carport);

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

    public static List<Order> getCustomerOrders(Context ctx, ConnectionPool connectionPool){
        User currentUser = ctx.sessionAttribute("currentUser");
        int customerId = currentUser.getId();
        List<Order> orders = new ArrayList<>();
        try {
            orders = OrderMapper.getAllCustomersOrders(customerId ,connectionPool);
        } catch (SQLException e) {
            ctx.attribute("message", e.getMessage());
        }
        return orders;
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
        Order order = ((List<Order>)ctx.sessionAttribute("customersOrderlist")).stream().filter(o -> o.getId() == orderID).collect(Collectors.toList()).get(0);

        List<Item> itemList = order.getCarport().getItemList().getItemList();

        HashMap<Item, Double> pricePerQuantityOfItem = new HashMap<>();

        for (Item item : itemList) {
            double price = item.price_pr_unit() * item.quantity();
            pricePerQuantityOfItem.put(item, price);
        }

        Order orderToShowWithDetails = new Order(order.getId(), order.getCustomerId(), order.getSalespersonId(), order.getDate(), order.getStatus(), order.getPrice(), order.getCarport(), pricePerQuantityOfItem);

        double num = order.getPrice();
        BigDecimal priceWithTwoDecimals = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
        CarportController.show2dDrawing(ctx, order);
        ctx.sessionAttribute("price", priceWithTwoDecimals);
        ctx.sessionAttribute("orderToShow", orderToShowWithDetails);
        
        

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

        Order order = ((List<Order>)ctx.sessionAttribute("salespersonOrderlist")).stream().filter(o -> o.getId() == orderID).collect(Collectors.toList()).get(0);


        List<Item> itemList = order.getCarport().getItemList().getItemList();

        HashMap<Item, Double> pricePerQuantityOfItem = new HashMap<>();

        for (Item item : itemList) {
            double price = item.price_pr_unit() * item.quantity();
            pricePerQuantityOfItem.put(item, price);
        }

        Order orderToShowWithDetails = new Order(order.getId(), order.getCustomerId(), order.getSalespersonId(), order.getDate(), order.getStatus(), order.getPrice(), order.getCarport(), pricePerQuantityOfItem);

        DecimalFormat df = new DecimalFormat("0.00");
        String priceWithTwoDecimals = df.format(order.getPrice());

        ctx.sessionAttribute("price", priceWithTwoDecimals);        
        ctx.sessionAttribute("orderToShow", orderToShowWithDetails);
        CarportController.show2dDrawing(ctx, order);
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

    public static void calculateNewOffer(Context ctx, ConnectionPool connectionPool) {

        Order orderEdited = ctx.sessionAttribute("orderToShow");

        String skur = Boolean.toString(orderEdited.getCarport().hasShed());
        Carport carport = CarportController.createCarport(skur, ctx, connectionPool);

        double price = CarportController.getPrice(carport);

        HashMap<Item, Double> pricePerQuantityOfItem = new HashMap<>();
        List<Item> itemList = orderEdited.getCarport().getItemList().getItemList();

        for (Item item : itemList) {
            double priceOfItemQuantity = item.price_pr_unit() * item.quantity();
            pricePerQuantityOfItem.put(item, priceOfItemQuantity);
        }

        orderEdited.setCarport(carport);
        orderEdited.setPrice(price);
        orderEdited.setPricePerQuantityOfItem(pricePerQuantityOfItem);

        DecimalFormat df = new DecimalFormat("0.00");
        String priceWithTwoDecimals = df.format(orderEdited.getPrice());
        ctx.sessionAttribute("price", priceWithTwoDecimals);

        try{
            OrderMapper.updateOrder(orderEdited, connectionPool);

            ctx.sessionAttribute("orderToShow", orderEdited);
            ctx.render("/salespersonOrderDetails.html");
        }
        catch (SQLException e){
            System.out.println("SQL EXCEPTION when calculating offer: " + e);
        }
        catch (OrderNotFoundException e){
            ctx.attribute("orderNotFound", e);
            System.out.println("ORDER NOT FOUND");
        }

    }
}


package app;

import com.fasterxml.jackson.databind.util.ClassUtil;

import app.config.ThymeleafConfig;
import app.controllers.CarportController;
import app.controllers.ItemController;
import app.controllers.OrderController;
import app.controllers.UserController;
import app.controllers.PageController;
import app.model.entities.Order;
import app.persistence.ConnectionPool;
import app.services.Calculator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fogcarport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    public static void main(String[] args) {

        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        Calculator.getInstance(connectionPool); // this is to initialise the singelton for the first time;

        // Routing
        app.get("/", ctx -> ctx.render("FrontPage.html"));

        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> UserController.login(ctx, connectionPool));
        app.post("/goToLoginPage", ctx -> PageController.goToLoginPage(ctx));

        app.get("/logout", ctx -> UserController.logout(ctx));

        app.get("/createUser", ctx -> ctx.render("createUser.html"));
        app.post("/createUser", ctx -> UserController.createUser(ctx, connectionPool));

        app.get("/carportSelection", ctx-> ctx.render("/carportSelection.html"));
        app.get("/adminpage", ctx -> ctx.render("/SellersPage.html"));
        app.get("/customerpage", ctx -> ctx.render("/customerPage.html"));

        app.get("/createOrder", ctx -> ctx.render("/carportSelection.html"));
        app.post("/createOrder", ctx -> OrderController.createOrder(ctx, connectionPool));
        app.get("/confirmOffer", ctx -> {
            CarportController.show2dDrawing(ctx, connectionPool);
            ctx.render("/confirmOfferRequest.html");
        });

        app.post("/offerRequested", ctx -> OrderController.placeOfferRequest(ctx, connectionPool));

        app.get("/customersAllOrdersPage", ctx -> ctx.render("/customersAllOrdersPage.html"));
        app.post("/customersAllOrders", ctx -> OrderController.customerSeeAllOrders(ctx, connectionPool));
        app.post("customerPaysForOrder", ctx -> OrderController.customerPaysForOrder(ctx, connectionPool));

        app.post("/customerSeeOrderDetails", ctx -> OrderController.customerSeeOrderDetails(ctx));
        app.post("/showPartsList", ctx -> OrderController.showPartsList(ctx));

        app.post("/sellersAllOrders", ctx -> OrderController.sellerSeeAllOrders(ctx, connectionPool));
        app.get("/sellers/EditOrder", ctx -> {OrderController.setupUpdatePage(ctx, connectionPool); ctx.render("updateOrder.html");});
        app.post("/sellers/EditOrder", ctx -> OrderController.updateOrderWidthOutShed(ctx, connectionPool));

        app.post("/salespersonTakeOrder", ctx-> OrderController.salespersonTakeOrder(ctx, connectionPool));
        app.post("/salespersonUntakeOrder", ctx-> OrderController.salespersonUntakeOrder(ctx, connectionPool));
        app.post("/salespersonSeeAssignedOrders", ctx -> OrderController.salespersonSeeAssignedOrders(ctx, connectionPool));
        app.post("/salespersonSeeOrderDetails", ctx-> OrderController.salespersonSeeOrderDetails(ctx, connectionPool));

        app.post("/calculateNewOffer", ctx -> OrderController.calculateNewOffer(ctx, connectionPool));

        app.post("/sendOffer", ctx -> OrderController.sendOffer(ctx, connectionPool));
        app.post("/salespersonEditItem", ctx -> ItemController.salespersonEditItem(ctx, connectionPool));
        app.post("/removeItem", ctx -> ItemController.removeItem(ctx, connectionPool));
        app.post("/addItem", ctx -> ItemController.addItem(ctx, connectionPool));
    }
}


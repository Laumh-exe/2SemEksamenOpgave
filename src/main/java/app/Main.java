package app;

import app.config.ThymeleafConfig;
import app.controllers.ItemController;
import app.controllers.OrderController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
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

        // Routing
        app.get("/", ctx -> ctx.render("FrontPage.html"));

        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> UserController.login(ctx, connectionPool));
        app.get("/logout", ctx -> UserController.logout(ctx));
        app.get("/createUser", ctx -> ctx.render("createUser.html"));
        app.post("/createUser", ctx -> UserController.createUser(ctx, connectionPool));

        app.get("/adminpage", ctx -> ctx.render("/SellersPage.html")); //TODO: make the sellers home page
        app.get("/customerpage", ctx -> ctx.render("/customerPage.html"));

        app.get("/createOrder", ctx -> ctx.render("/carportSelection.html"));
        app.post("/createOrder", ctx -> OrderController.createOrder(ctx, connectionPool));
        app.get("/confirmOffer", ctx -> ctx.render("/confirmOfferRequest.html"));

        app.post("/offerRequested", ctx -> OrderController.placeOrder(ctx, connectionPool));

        app.get("/customersAllOrdersPage", ctx -> ctx.render("/customersAllOrdersPage.html"));
        app.post("/customersAllOrders", ctx -> OrderController.customerSeeAllOrders(ctx, connectionPool));
        app.post("/customerSeeOrderDetails", ctx -> OrderController.customerSeeOrderDetails(ctx));

        app.get("/sellers/AllOrders", ctx -> OrderController.sellerSeeAllOrders(ctx, connectionPool));
        app.get("/sellers/EditOrder", ctx -> {OrderController.setupUpdatePage(ctx, connectionPool); ctx.render("updateOrder.html");});
        app.post("/sellers/EditOrder", ctx -> OrderController.updateOrderWidthOutShed(ctx, connectionPool));
        app.post("/removeItem", ctx -> ItemController.removeItem(ctx, connectionPool));
        app.post("/addItem", ctx -> ItemController.addItem(ctx, connectionPool));
    }
}


package app;

import app.config.ThymeleafConfig;

import app.controllers.OrderController;
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
        app.get("/", ctx -> ctx.render("index.html"));

        app.post("/offerRequested", ctx -> OrderController.placeOrderInDB(ctx, connectionPool));
        
        app.get("/sellers/AllOrders", ctx -> OrderController.sellerSeeAllOrders(ctx, connectionPool));

    }
}


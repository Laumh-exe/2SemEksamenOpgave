package app.controllers;

import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;
import app.model.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class UserController {

    public static void login(Context ctx, ConnectionPool connectionPool) {

        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);

            if (user.getRole().equals("salesperson")) {
                ctx.redirect("/adminpage");
            } else {
                Order order = ctx.sessionAttribute("order");
                if(order != null){
                    ctx.redirect("/confirmOffer");
                } else {
                    ctx.redirect("/customerpage");
                }
            }

        } catch (SQLException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }
    }

    public static void logout(Context ctx) {
        // Invalidate session
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void createUser(Context ctx, ConnectionPool connectionPool) {
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
       // String role = ctx.formParam("salesperson");

        try {
            UserMapper.createUser(firstName, lastName, email, password, connectionPool);
            ctx.render("login.html");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ctx.render("createUser.html");
        }
    }

    public static void customerSetup(Context ctx, ConnectionPool connectionPool) {
        List<Order> orders = OrderController.getCustomerOrders(ctx, connectionPool);
        ItemList il = new ItemList();
        il.add(new Item(1, 100, 10, "yes", "spær", 20));
        il.add(new Item(2, 100, 10, "yes", "stolbe", 10));
        orders.get(0).getCarport().setItemList(il);

        
        ctx.sessionAttribute("order", orders.get(0));
        CarportController.show2dDrawing(ctx, connectionPool);
    }


}

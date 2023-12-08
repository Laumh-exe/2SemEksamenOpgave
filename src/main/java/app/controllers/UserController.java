package app.controllers;

import app.entities.Item;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                ctx.redirect("/customerpage");
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


}

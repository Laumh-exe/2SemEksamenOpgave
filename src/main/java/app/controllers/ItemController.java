package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import app.persistence.ItemMapper;

import java.sql.SQLException;

public class ItemController {

    public static void addItem(Context ctx, ConnectionPool connectionPool){
        String price_pr_unit = ctx.formParam("price_pr_unit");
        String length = ctx.formParam("length");
        String unit = ctx.formParam("unit");
        String description = ctx.formParam("description");

        try{
            ItemMapper.addItem(price_pr_unit, length, unit, description);
            ctx.render("item.html");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            ctx.render("item.html");
        }

    }
}

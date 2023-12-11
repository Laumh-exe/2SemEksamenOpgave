package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import app.persistence.ItemMapper;

import java.sql.SQLException;

public class ItemController {

    public static void addItem(Context ctx, ConnectionPool connectionPool){
        double price_pr_unit = Double.parseDouble(ctx.formParam("price_pr_unit"));
        double length = Double.parseDouble(ctx.formParam("length"));
        String unit = ctx.formParam("unit");
        String description = ctx.formParam("description");

        try{
            ItemMapper.addItem(price_pr_unit, length, unit, description, connectionPool);
            ctx.render("item.html");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            ctx.render("item.html");
        }

    }
}

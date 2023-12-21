package app.controllers;

import app.model.entities.Item;
import app.model.entities.ItemList;
import app.persistence.ConnectionPool;
import app.persistence.ItemMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    public static void addItem(Context ctx, ConnectionPool connectionPool) {
        //int id = Integer.parseInt(ctx.formParam("id"));
        double price_pr_unit = Double.parseDouble(ctx.formParam("price_pr_unit"));
        double length = Double.parseDouble(ctx.formParam("length"));
        String unit = ctx.formParam("unit");
        String description = ctx.formParam("description");
        String function = ctx.formParam("function");

        try {
            ItemMapper.addItem(price_pr_unit, length, unit, description, function, connectionPool);
            List<Item> itemlist = ItemMapper.getAllItems(connectionPool);
            ctx.sessionAttribute("itemlist", itemlist);
            ctx.render("item.html");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ctx.render("item.html");
        }
    }


    public static void removeItem(Context ctx, ConnectionPool connectionPool) {
        int id = Integer.parseInt(ctx.formParam("item_id"));
        try {
            ItemMapper.removeItem(id, connectionPool);
            List<Item> itemlist = ItemMapper.getAllItems(connectionPool);
            ctx.sessionAttribute("itemlist", itemlist);
            ctx.render("item.html");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ctx.render("item.html");
        }
    }

    public static List<Item> getAllItems(ConnectionPool connectionPool) throws SQLException {
        List<Item> allItems = null;
        try {
            allItems = ItemMapper.getAllItems(connectionPool);
            //  ctx.sessionAttribute("allItem", allItem);
            return allItems;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public static void salespersonEditItem(Context ctx, ConnectionPool connectionPool) throws SQLException {
        List<Item> allItems = getAllItems(connectionPool);
        ctx.sessionAttribute("itemlist",allItems);
        ctx.render("item.html");
    }
}

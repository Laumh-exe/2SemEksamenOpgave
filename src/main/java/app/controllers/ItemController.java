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

        try {
            ItemMapper.addItem(price_pr_unit, length, unit, description, connectionPool);
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


	public static List<Item> getAllItems() {
        ArrayList<Item> il = new ArrayList<>();
        il.add(new Item(1, 100, 10, "yes", "spær", 20));
        il.add(new Item(2, 100, 10, "yes", "stolbe", 10));
        return il;
	}
}

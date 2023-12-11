package app.persistence;

import app.exceptions.DatabaseException;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemMapper {




    public static Boolean placeItemListInDB(Order order, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO items_orders (order_id, item_id, quantity) VALUES ";


        for (Item item : order.getCarport().getItemList().getItemList()) {

            if (order.getCarport().getItemList().getItemList().indexOf(item) != 0) {
                sql += ",";
            }

            sql += "(" + order.getId() + "," + item.id() + "," + item.quantity() + ")";
        }



        System.out.println(sql);

            try (Connection connection = connectionPool.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {

                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected == order.getCarport().getItemList().getItemList().size()) {
                        System.out.println("itemlist placed");

                    } else {
                        System.out.println("Itemlist NOT placed");
                        throw new DatabaseException("Item line not inserted in DB");
                    }
                }
            } catch (SQLException e) {
                System.out.println("sql EXCEPTION");

            }

        return true;
    }
    
}

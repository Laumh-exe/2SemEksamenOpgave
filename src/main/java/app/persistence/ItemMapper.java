package app.persistence;

import app.exceptions.DatabaseException;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemMapper {




    public static Boolean placeItemListInDB(ItemList itemList, Order order, ConnectionPool connectionPool) throws DatabaseException {

        for (Item item : itemList.getItemList()) {

            String sql = "INSERT INTO items_orders (order_id, item_id, quantity) " +
                    "VALUES (?, ?, ?)";

            try (Connection connection = connectionPool.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {

                    ps.setInt(1, order.getId());
                    ps.setInt(2, item.id());
                    ps.setInt(3, item.quantity());

                    int rowsAffected = ps.executeUpdate();

                    if (rowsAffected == 1) {
                        return true;

                    } else {
                        throw new DatabaseException("Item line not inserted in DB");
                    }
                }
            } catch (SQLException e) {


            }
        }
        return false;
    }
    
}

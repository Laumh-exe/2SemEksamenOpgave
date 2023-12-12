package app.persistence;


import app.exceptions.DatabaseException;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;
import app.model.entities.*;


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
    

    public static void addItem(double price_pr_unit, double length, String unit, String description, ConnectionPool connectionPool) throws SQLException {
        String sql = "INSERT INTO public.item (unit, description, length, price_pr_unit)" +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, unit);
                preparedStatement.setString(2, description);
                preparedStatement.setDouble(3, length);
                preparedStatement.setDouble(4, price_pr_unit);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected != 1) {
                    throw new SQLException("Fejl");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("FEJL!!");
        }
    }

}

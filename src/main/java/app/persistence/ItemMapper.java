package app.persistence;


import app.exceptions.DatabaseException;
import app.model.entities.Item;
import app.model.entities.ItemList;
import app.model.entities.Order;
import app.model.entities.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

                } else {
                    throw new DatabaseException("Item line not inserted in DB");
                }
            }
        } catch (SQLException e) {
            System.out.println("sql EXCEPTION");
        }
        return true;
    }


    public static void addItem(double price_pr_unit, double length, String unit, String description,String function, ConnectionPool connectionPool) throws SQLException {
        String sql = "INSERT INTO public.item (price_pr_unit, length, unit, function, description)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setDouble(1, price_pr_unit);
                preparedStatement.setDouble(2, length);
                preparedStatement.setString(3, unit);
                preparedStatement.setString(4, description);
                preparedStatement.setString(5, function);


                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected != 1) {
                    throw new SQLException("Fejl");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("FEJL!!");
        }
    }

    public static List<Item> getAllItems(ConnectionPool connectionPool) throws SQLException {

        String sql = "SELECT * FROM public.item";

        ArrayList<Item> item = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {

                    int id = rs.getInt("id");
                    double price_pr_unit = rs.getDouble("price_pr_unit");
                    double length = rs.getDouble("length");
                    String unit = rs.getString("unit");
                    String description = rs.getString("description");
                    String function = rs.getString("function");

                    item.add(new Item(id, price_pr_unit, length, unit, description, function));
                }
            }
        }
        return item;
    }

    public static void removeItem(int id, ConnectionPool connectionPool) throws SQLException {

        String sql = "DELETE FROM public.item WHERE id=?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
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


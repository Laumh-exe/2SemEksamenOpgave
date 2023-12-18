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


    public static Boolean placeItemListInDB(Order order, ConnectionPool connectionPool) throws SQLException{


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


                if (rowsAffected != order.getCarport().getItemList().getItemList().size()) {
                    throw new SQLException("Item line not inserted in DB");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Something went wrong with placing ItemList in DB");

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
                preparedStatement.setString(4, function);
                preparedStatement.setString(5, description);


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


    public static ItemList getItemlistsForOrders(int orderId, ConnectionPool connectionPool) throws SQLException {

        String sql = "SELECT item.id, item.unit, item.function, \n" +
                "item.description, item.length, item.price_pr_unit, items_orders.quantity\n" +
                "FROM items_orders \n" +
                "JOIN item ON items_orders.item_id = item.id\n" +
                "WHERE order_id = ?; ";

        ItemList itemList = new ItemList();

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, orderId);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int item_id = resultSet.getInt("id");
                    int quantity = resultSet.getInt("quantity");
                    String unit = resultSet.getString("unit");
                    String carport_part = resultSet.getString("function");
                    int length = resultSet.getInt("length");
                    double price_pr_unit = resultSet.getInt("price_pr_unit");
                    String description = resultSet.getString("description");

                    Item item = new Item(item_id, price_pr_unit, length, unit, description, quantity, carport_part);
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }

    public static void removeItemListInDB(Order order, ConnectionPool connectionPool) throws SQLException{

        String sql = "DELETE FROM items_orders WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, order.getId());

                preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            throw new SQLException("FEJL!!");
        }


    }
}


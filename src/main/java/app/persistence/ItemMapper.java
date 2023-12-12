package app.persistence;

import app.model.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static void addItem(double price_pr_unit, double length, String unit, String description, ConnectionPool connectionPool) throws SQLException {
        String sql = "INSERT INTO public.item (price_pr_unit, length, unit, description)" +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(4, price_pr_unit);
                preparedStatement.setDouble(3, length);
                preparedStatement.setString(1, unit);
                preparedStatement.setString(2, description);


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
        String sql = "SELECT id, price_pr_unit, length, unit, description FROM public.item";
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
                    item.add(new Item(price_pr_unit, length, unit, description));
                }
            }
        }
        return item;
    }

    public static void removeItem(){
        
    }
}
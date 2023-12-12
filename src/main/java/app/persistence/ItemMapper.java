package app.persistence;

import app.model.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemMapper {

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

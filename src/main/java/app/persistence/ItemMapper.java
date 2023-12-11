package app.persistence;

import app.model.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemMapper {

    public static void addItem(double price_pr_unit, double length, String unit, String description, ConnectionPool connectionPool) throws SQLException {
        String sql = "INSERT INTO \"item\" (price_pr_unit, length, unit, description";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDouble(1, price_pr_unit);
                preparedStatement.setDouble(2, length);
                preparedStatement.setString(3, unit);
                preparedStatement.setString(4, description);
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

package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.entities.*;


public class UserMapper {



    public static User login(String email, String password, ConnectionPool connectionPool) throws SQLException {
        String sql = "SELECT * FROM public.customer, public.salesperson WHERE email=? AND password=?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String role = resultSet.getString("role");
                    double balance = resultSet.getDouble("balance");

                    return new Customer(id, firstName, lastName, email, password, role, balance);
                } else {
                    throw new SQLException("Fejl i login");
                }
            }
        }
    }

    public static void createUser(String firstName, String lastName, String email, String password, ConnectionPool connectionPool) throws SQLException {
        boolean emailExists = checkIfEmailExists(email, connectionPool);
        if(emailExists){
            throw new SQLException("Email findes allerede");
        } else {
        String sql = "INSERT INTO \"customer\" (firstName, lastName, email, password, role, balance) VALUES (?, ?, ?, 200)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, password);
                preparedStatement.setString(5, "customer");
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected != 1) {
                    throw new SQLException("Fejl ved at oprette en ny bruger");
                }
            }

        } catch (SQLException e) {
            throw new SQLException("FEJL!!");
        }
        }
    }

    public static boolean checkIfEmailExists(String email, ConnectionPool connectionPool) throws SQLException {
        String sql = "SELECT * FROM public.customer, public.salesperson WHERE email=?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return true;
                }

            }
            catch (SQLException e){
                throw new SQLException("Fejl i check");
            }
        }
        return false;
    }
}
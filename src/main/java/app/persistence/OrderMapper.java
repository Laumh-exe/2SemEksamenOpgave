package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.entities.*;
import app.exceptions.DatabaseException;


public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) {
        String sql = "SELECT * FROM public.order";
        List<Order> orders = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String statusString = resultSet.getString("status");
                    Date date = resultSet.getDate("date");
                    int customerId = resultSet.getInt("customer_id");
                    int salespersonId = resultSet.getInt("salesperson_id");
                    double price = resultSet.getDouble("total_price");
                    double carportLength = resultSet.getDouble("carport_length");
                    double carportWidth = resultSet.getDouble("carport_width");
                    int shedLength = resultSet.getInt("shed_length");
                    int shedWidth = resultSet.getInt("shed_width");

                    Shed newShed = new Shed(shedLength, shedWidth);
                    Carport carport = new Carport(carportLength, carportWidth, newShed);
                    
                    OrderStatus status = OrderStatus.valueOf(statusString);
                    Order order = new Order(id, customerId, salespersonId, date, status, price, carport);
                    orders.add(order);
                }
            }
        } catch (SQLException e){

        }
        return orders;
    }


    public static Boolean placeOrder(User currentUser, Order order, ConnectionPool connectionPool) throws DatabaseException{

        String sql = "INSERT INTO order (status, date, customer_id, total_price, " +
                "carport_width, carport_length, shed_width, shed_length, salesperson_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // TODO: Delete this and make it work with order.getDate()
        long millis = System.currentTimeMillis();
        java.sql.Date dateOfOrder = new java.sql.Date(millis);

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, order.getStatus().toString());
                ps.setDate(2, dateOfOrder);
                ps.setInt(3, currentUser.getId());
                ps.setDouble(4, 0);
                ps.setDouble(5, order.getCarport().getWidth());
                ps.setDouble(6, order.getCarport().getLength());
                ps.setDouble(7, order.getCarport().getShed().getWidth());
                ps.setDouble(8, order.getCarport().getShed().getLength());
                ps.setInt(9, 0);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1) {
                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    int generatedOrderId = rs.getInt(1);

                } else {
                    return false;
                }
            }
        } catch (SQLException e) {

        }
        return true;
    }
}

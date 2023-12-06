package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.entities.Order;
import app.entities.OrderStatus;
import app.exceptions.DatabaseException;
import app.entities.User;



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
                    double shedLength = resultSet.getDouble("shed_length");
                    double shedWidth = resultSet.getDouble("shed_width");
                    
                    OrderStatus status = OrderStatus.valueOf(statusString);
                    Order order = new Order(id, customerId, salespersonId, date, status, price, carportLength, carportWidth, shedLength, shedWidth);
                    orders.add(order);
                }
            }
        } catch (SQLException e){

        }
        return orders;
    }


    public static void placeOrder(User currentUser, Order order, ConnectionPool connectionPool) throws DatabaseException{

        String sql = "INSERT INTO order (status, date, customer_id, total_price, " +
                "carport_width, carport_length, shed_width, shed_length, salesperson_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, order.getStatus());
                preparedStatement.setDate(2, order.getDate());
                preparedStatement.setInt(3, currentUser.getId());
                preparedStatement.setDouble(4, 0);
                preparedStatement.setDouble(5, order.getCarport().getWidth());
                preparedStatement.setDouble(6, order.getCarport().getLength());
                preparedStatement.setDouble(7, order.getCarport().getShed().getWidth());
                preparedStatement.setDouble(8, order.getCarport().getShed().getLength());
                preparedStatement.setInt(9, 0);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 1) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    rs.next();
                    generatedOrderId = rs.getInt(1);

                } else {
                    throw new DatabaseException("Fejl");
                }



            }
        } catch (SQLException e) {

        }
    }


}

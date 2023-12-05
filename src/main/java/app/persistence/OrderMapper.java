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

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();

        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    OrderStatus status = OrderStatus.valueOf(resultSet.getString("sataus"));
                    Date date = resultSet.getDate("date");
                    int customerId = resultSet.getInt("customer_id");
                    int salespersonId = resultSet.getInt("salesperson_id");
                    double price = resultSet.getDouble("total_price");
                    double carportLength = resultSet.getDouble("carport_length");
                    double carportWidth = resultSet.getDouble("carport_width");
                    double shedLength = resultSet.getDouble("shed_length");
                    double shedWidth = resultSet.getDouble("shed_width");
                    

                    Order order = new Order(id, date, status, price, carportLength, carportWidth, shedLength, shedWidth);
                    orders.add(order);
                }
            }
        } catch (SQLException e){

        }
        return orders;
    }
}

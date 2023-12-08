package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.entities.Carport;
import app.entities.Order;
import app.entities.OrderStatus;
import app.entities.Shed;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws SQLException {
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
                    Order order = new Order(id, customerId, salespersonId, date, status, price, new Carport(carportLength, carportWidth, new Shed(shedLength, shedWidth)));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * This method is for orders with out sheds
     * @param order
     * @param connectionPool
     * @throws SQLException
     */
    public static void updateOrderWidthOutShed(Order order, ConnectionPool connectionPool) throws SQLException {
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width) = (?, ?, ?, ?) WHERE id = ?";
        Carport carport = order.getCarport();
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, order.getStatus().toString());
                preparedStatement.setDouble(2, order.getPrice());
                preparedStatement.setDouble(3, carport.getLength());
                preparedStatement.setDouble(4, carport.getWidth());
                preparedStatement.setInt(5, order.getId());
                
                int numRowsAffected = preparedStatement.executeUpdate();
                if (numRowsAffected > 1){
                    // TODO: do something meaningfull when more than one order is affected
                }
                
            }
        }
    }

    public static void updateOrderWidthShed(Order order, ConnectionPool connectionPool) throws SQLException {
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width, shed_length, shed_width) = (?, ?, ?, ?, ?, ?) WHERE id = ?";
        Carport carport = order.getCarport();
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, order.getStatus().toString());
                preparedStatement.setDouble(2, order.getPrice());
                preparedStatement.setDouble(3, carport.getLength());
                preparedStatement.setDouble(4, carport.getWidth());
                // TODO: When shed is implemented this needs to be updated to reflect the needed shed
                // preparedStatement.setDouble(5, carport.getShed().getLength());
                // preparedStatement.setDouble(6, carport.getShed().getWidth());
                preparedStatement.setInt(7, order.getId());

                int numRowsAffected = preparedStatement.executeUpdate();
                if (numRowsAffected > 1){
                    // TODO: do something meaningfull when more than one order is affected
                }
            }
        }
    }
}

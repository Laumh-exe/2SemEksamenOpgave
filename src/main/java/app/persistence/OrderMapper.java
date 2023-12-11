package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.model.entities.*;
import app.exceptions.DatabaseException;


import app.model.entities.*;
import app.exceptions.OrderNotFoundException;


public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws SQLException {
        String sql = "SELECT * FROM public.order";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String statusString = resultSet.getString("status");
                    java.sql.Date date = resultSet.getDate("date");
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


    public static Boolean placeOrder(User currentUser, Order order, ItemList itemlist, ConnectionPool connectionPool)
            throws DatabaseException {

        Order orderPlacedInDB = placeOrderInDB(currentUser, order, connectionPool);

        ItemMapper.placeItemListInDB(itemlist, order, connectionPool);


        return true;
    }

    public static Order placeOrderInDB(User currentUser, Order order, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO public.order (status, date, customer_id, total_price, " +
                "carport_width, carport_length, shed_width, shed_length, salesperson_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Date utilDate = order.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, order.getStatus().toString());
                ps.setDate(2, sqlDate);
                ps.setInt(3, currentUser.getId());
                ps.setDouble(4, 0);
                ps.setDouble(5, order.getCarport().getWidth());
                ps.setDouble(6, order.getCarport().getLength());
                ps.setDouble(7, order.getCarport().getShed().getWidth());
                ps.setDouble(8, order.getCarport().getShed().getLength());
                // TODO: Decide what to do with this salesperson ID. Should it just be null in DB?
                ps.setInt(9, 1);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected == 1) {

                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    int generatedOrderId = rs.getInt(1);

                    Order orderWithId = new Order(generatedOrderId, order.getCustomerId(),
                            order.getSalespersonId(), sqlDate, order.getStatus(),
                            order.getPrice(), order.getCarport());
                    return orderWithId;

                } else {
                    throw new DatabaseException("Order not inserted");
                }
            }
        } catch (SQLException e) {

        }
        return order;
    }





    /**
     * This method is for orders with out sheds
     *
     * @param order
     * @param connectionPool
     * @throws SQLException
     * @throws OrderNotFoundException
     */
    public static void updateOrderWidthOutShed(Order order, ConnectionPool connectionPool) throws SQLException, OrderNotFoundException {
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width) = (?, ?, ?, ?) WHERE id = ?";
        Carport carport = order.getCarport();
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, order.getStatus().toString());
                preparedStatement.setDouble(2, order.getPrice());
                preparedStatement.setDouble(3, carport.getLength());
                preparedStatement.setDouble(4, carport.getWidth());
                preparedStatement.setInt(5, order.getId());

                int numRowsAffected = preparedStatement.executeUpdate();
                if (numRowsAffected > 1) {
                    // TODO: do something meaningfull when more than one order is affected
                }
                if (numRowsAffected < 1) {
                    throw new OrderNotFoundException("Order with id:" + order.getId() + " was not found");
                }

            }
        }
    }

    public static void updateOrderWidthShed(Order order, ConnectionPool connectionPool) throws SQLException, OrderNotFoundException {
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width, shed_length, shed_width) = (?, ?, ?, ?, ?, ?) WHERE id = ?";
        Carport carport = order.getCarport();
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, order.getStatus().toString());
                preparedStatement.setDouble(2, order.getPrice());
                preparedStatement.setDouble(3, carport.getLength());
                preparedStatement.setDouble(4, carport.getWidth());
                // TODO: When shed is implemented this needs to be updated to reflect the needed shed
                // preparedStatement.setDouble(5, carport.getShed().getLength());
                // preparedStatement.setDouble(6, carport.getShed().getWidth());
                preparedStatement.setInt(7, order.getId());

                int numRowsAffected = preparedStatement.executeUpdate();
                if (numRowsAffected > 1) {
                    // TODO: do something meaningfull when more than one order is affected
                }
                if (numRowsAffected < 1) {
                    throw new OrderNotFoundException("Order with id:" + order.getId() + " was not found");
                }
            }
        }

    }
}

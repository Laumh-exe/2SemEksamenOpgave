package app.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.model.entities.*;

import app.exceptions.DatabaseException;
import app.exceptions.OrderNotFoundException;
import app.model.entities.*;


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


    public static Boolean placeOrder(User currentUser, Order order, ConnectionPool connectionPool)
            throws DatabaseException {

        Order orderPlacedInDB = placeOrderInDB(currentUser, order, connectionPool);

        ItemMapper.placeItemListInDB(orderPlacedInDB, connectionPool);

        return true;
    }

    public static Order placeOrderInDB(User currentUser, Order order, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "";

        System.out.println("Shed or no shed: " + order.getCarport().isShed());

        if (order.getCarport().isShed()) {

            System.out.println("Sql with shed");
            sql = "INSERT INTO public.order (status, date, customer_id, total_price, " +
                    "carport_width, carport_length, shed_width, shed_length) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        } else {
            System.out.println("Sql without shed");
            sql = "INSERT INTO public.order (status, date, customer_id, total_price, " +
                    "carport_width, carport_length) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";


        }


        Date utilDate = order.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                System.out.println("Inserting order in DB");
                ps.setString(1, order.getStatus().toString());
                ps.setDate(2, sqlDate);
                ps.setInt(3, currentUser.getId());
                ps.setDouble(4, 0);
                ps.setDouble(5, order.getCarport().getWidth());
                ps.setDouble(6, order.getCarport().getLength());

                if (order.getCarport().isShed()) {
                    ps.setDouble(7, order.getCarport().getShed().getWidth());
                    ps.setDouble(8, order.getCarport().getShed().getLength());
                }

                int rowsAffected = ps.executeUpdate();

                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected == 1) {

                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    int generatedOrderId = rs.getInt(1);

                    Order orderWithId = new Order(generatedOrderId, order.getCustomerId(),
                            order.getSalespersonId(), sqlDate, order.getStatus(),
                            order.getPrice(), order.getCarport());

                    return orderWithId;

                } else {
                    System.out.println("Order not inserted");
                    throw new DatabaseException("Order not inserted");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception in placeOrderInDB");

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

    public static List<Order> getAllCustomersOrders(int id, ConnectionPool connectionPool) throws SQLException {

        String sql = "SELECT * FROM public.order WHERE customer_id = ?";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int order_id = resultSet.getInt("id");
                    String statusString = resultSet.getString("status");
                    java.sql.Date date = resultSet.getDate("date");

                    int salespersonId = resultSet.getInt("salesperson_id");
                    double price = resultSet.getDouble("total_price");
                    double carportLength = resultSet.getDouble("carport_length");
                    double carportWidth = resultSet.getDouble("carport_width");
                    double shedLength = resultSet.getDouble("shed_length");
                    double shedWidth = resultSet.getDouble("shed_width");

                    OrderStatus status = OrderStatus.valueOf(statusString);
                    Order order = new Order(order_id, id, salespersonId, date, status, price, new Carport(carportLength, carportWidth, new Shed(shedLength, shedWidth)));

                    orders.add(order);
                }
            }
        }
        return orders;
    }
}

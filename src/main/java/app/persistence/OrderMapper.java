package app.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.model.entities.*;

import app.exceptions.OrderNotFoundException;


public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws SQLException {
        String sql = "SELECT * FROM public.order\n" +
                "ORDER BY public.order.id DESC;";
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
                    System.out.println("Salesperson ID: " + salespersonId);
                    Order order = new Order(id, customerId, salespersonId, date, status, price, new Carport(carportLength, carportWidth, new Shed(shedLength, shedWidth)));

                    orders.add(order);
                }
            }
        }
        return orders;
    }


    public static Boolean placeOrder(User currentUser, Order order, ConnectionPool connectionPool)
            throws SQLException {

        Order orderPlacedInDB = placeOrderInDB(currentUser, order, connectionPool);

        ItemMapper.placeItemListInDB(orderPlacedInDB, connectionPool);

        return true;
    }

    public static Order placeOrderInDB(User currentUser, Order order, ConnectionPool connectionPool) throws SQLException {

        String sql = "";

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

                if (rowsAffected == 1) {

                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    int generatedOrderId = rs.getInt(1);

                    Order orderWithId = new Order(generatedOrderId, order.getCustomerId(),
                            order.getSalespersonId(), sqlDate, order.getStatus(),
                            order.getPrice(), order.getCarport());

                    return orderWithId;
                }
            }
        } catch (SQLException e) {

            throw new SQLException("Something went wrong with placing order in DB");

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

        List<Order> allOrderWithoutItemlist = getAllCustomersOrdersWithoutItemList(id, connectionPool);

        List<Order> allOrdersWithItemlist = new ArrayList<>();

        for (Order order : allOrderWithoutItemlist) {

            Carport carportWithItemlist = null;

            ItemList itemlistForOrder = ItemMapper.getItemlistsForOrders(order.getId(), connectionPool);

            if (order.getCarport().isShed()) {
                carportWithItemlist = new Carport(order.getCarport().getLength(), order.getCarport().getWidth(), order.getCarport().getShed(), itemlistForOrder);
            } else {
                carportWithItemlist = new Carport(order.getCarport().getLength(), order.getCarport().getWidth(), itemlistForOrder);
            }

            Order orderWithItemlist = new Order(order.getId(), order.getCustomerId(), order.getSalespersonId(), order.getDate(), order.getStatus(), order.getPrice(), carportWithItemlist);
            allOrdersWithItemlist.add(orderWithItemlist);
        }

        return allOrdersWithItemlist;
    }

    public static List<Order> getAllCustomersOrdersWithoutItemList(int id, ConnectionPool connectionPool) throws SQLException {

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

    public static void setStatusOfOrderInDB(Order order, ConnectionPool connectionPool) throws SQLException {

        String sql = "UPDATE public.order SET status = ? WHERE id= ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, order.getStatus().toString());
                preparedStatement.setInt(2, order.getId());
                int numRowsAffected = preparedStatement.executeUpdate();
            }
        }

    }

    public static void salespersonTakeOrder(int orderId, User salesperson, ConnectionPool connectionPool) throws SQLException {

        String sql = "UPDATE public.order SET status = ?, salesperson_id = ? WHERE id= ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, OrderStatus.ORDER_ASSIGNED.toString());
                preparedStatement.setInt(2, salesperson.getId());
                preparedStatement.setInt(3, orderId);
                int numRowsAffected = preparedStatement.executeUpdate();
            }
        }
    }

    public static void salespersonUntakeOrder(int orderId, User salesperson, ConnectionPool connectionPool) throws SQLException {

        String sql = "UPDATE public.order SET status = ?, salesperson_id = ? WHERE id= ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, OrderStatus.CUSTOMER_ACCEPTED.toString());
                preparedStatement.setNull(2, 0);
                preparedStatement.setInt(3, orderId);
                int numRowsAffected = preparedStatement.executeUpdate();

            }
        }
    }

    public static List<Order> getAllSalespersonsOrders(int id, ConnectionPool connectionPool) throws SQLException{

        List<Order> allOrderWithoutItemlist = getAllSalespersonsOrdersWithoutItemList(id, connectionPool);

        List<Order> allOrdersWithItemlist = new ArrayList<>();

        for (Order order : allOrderWithoutItemlist) {

            Carport carportWithItemlist = null;

            ItemList itemlistForOrder = ItemMapper.getItemlistsForOrders(order.getId(), connectionPool);

            if (order.getCarport().isShed()) {
                carportWithItemlist = new Carport(order.getCarport().getLength(), order.getCarport().getWidth(), order.getCarport().getShed(), itemlistForOrder);
            } else {
                carportWithItemlist = new Carport(order.getCarport().getLength(), order.getCarport().getWidth(), itemlistForOrder);
            }

            Order orderWithItemlist = new Order(order.getId(), order.getCustomerId(), order.getSalespersonId(), order.getDate(), order.getStatus(), order.getPrice(), carportWithItemlist);
            allOrdersWithItemlist.add(orderWithItemlist);
        }

        return allOrdersWithItemlist;
    }

    private static List<Order> getAllSalespersonsOrdersWithoutItemList(int id, ConnectionPool connectionPool) throws SQLException{
        String sql = "SELECT * FROM public.order WHERE salesperson_id = ?";
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


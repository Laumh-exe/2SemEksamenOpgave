package app.controllers;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import app.model.entities.Carport;
import app.model.entities.Order;
import app.model.entities.OrderStatus;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

public class OrderControllerTest {
    
    private ConnectionPool connectionPool;
    private Connection connection;
    private PreparedStatement ps;
    ResultSet rs;
    private Context ctx;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() throws SQLException{
        
        connectionPool = mock(ConnectionPool.class);
        ctx = mock(Context.class);
        connection = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
    }
    
    private void getAllTestSetup() throws SQLException {
        String sql = "SELECT * FROM public.order";
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(rs.getInt("id")).thenReturn(1).thenReturn(2);
        Mockito.when(rs.getString("status")).thenReturn("READY_FOR_REVIEW").thenReturn("PRICE_PRESENTED");
        Mockito.when(rs.getDate("date")).thenReturn(java.sql.Date.valueOf("2023-12-20")).thenReturn(java.sql.Date.valueOf("2023-12-21"));
        Mockito.when(rs.getInt("customer_id")).thenReturn(1).thenReturn(1);
        Mockito.when(rs.getInt("salesperson_id")).thenReturn(1).thenReturn(1);
        Mockito.when(rs.getDouble("total_price")).thenReturn(11500d).thenReturn(100.1);
        Mockito.when(rs.getDouble("carport_width")).thenReturn(10d).thenReturn(100d);
        Mockito.when(rs.getDouble("carport_length")).thenReturn(10d).thenReturn(20d);
        Mockito.when(rs.getDouble("shed_width")).thenReturn(-1d).thenReturn(10d);
        Mockito.when(rs.getDouble("shed_length")).thenReturn(-1d).thenReturn(10d);
    }
    
    @AfterEach
    public void tearDown(){
        connectionPool = null;
        ctx = null;
    }

    @Test
    public void testAllOrders() throws SQLException{
        
        //arrange
        getAllTestSetup();
        
        //act
        OrderController.sellerSeeAllOrders(ctx, connectionPool);
        
        //assert
        getAllTestSetup();
        InOrder inOrder = inOrder(ctx);
        inOrder.verify(ctx).sessionAttribute("allOrders", OrderMapper.getAllOrders(connectionPool));
        inOrder.verify(ctx).render("SellersAllOrders.html");
    }

    @Test
    public void testUpdateOrderWithoutShed() throws SQLException{
        Order order = new Order(1, 1, 1, Date.from(Instant.now()), OrderStatus.ORDER_ASSIGNED, 0, new Carport(100d,100d));
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width) = (?, ?, ?, ?) WHERE id = ?";
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        Mockito.when(ctx.sessionAttribute("order")).thenReturn(order);
        Mockito.when(ctx.formParam("salespersonId")).thenReturn("1");
        Mockito.when(ctx.formParam("newCarprotLength")).thenReturn("100");
        Mockito.when(ctx.formParam("newCarprotWidth")).thenReturn("100");
        Mockito.when(ctx.formParam("newStatus")).thenReturn(OrderStatus.ORDER_ASSIGNED.toString());
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeUpdate()).thenReturn(1);

        OrderController.updateOrderWidthOutShed(ctx, connectionPool);
        
        InOrder inOrderCtx = Mockito.inOrder(ctx);
        inOrderCtx.verify(ctx).sessionAttribute("order");
        inOrderCtx.verify(ctx).render("updateOrder.html");
        ;

        InOrder inOrderPs = Mockito.inOrder(ps);
        inOrderPs.verify(ps).setString(1, order.getStatus().toString());
        inOrderPs.verify(ps).setDouble(2, order.getPrice());
        inOrderPs.verify(ps).setDouble(3, order.getCarport().getLengthMeter());
        inOrderPs.verify(ps).setDouble(4, order.getCarport().getWidthMeter());
        inOrderPs.verify(ps).setInt(5, order.getId());
    }

    @Test
    public void testUpdateOrderWithoutErrorHandelingShed() throws SQLException{
        Order order = new Order(1, 1, 1, Date.from(Instant.now()), OrderStatus.ORDER_ASSIGNED, 0, new Carport(100d,100d));
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width) = (?, ?, ?, ?) WHERE id = ?";
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        Mockito.when(ctx.sessionAttribute("order")).thenReturn(order);
        Mockito.when(ctx.formParam("salespersonId")).thenReturn("1");
        Mockito.when(ctx.formParam("newCarprotLength")).thenReturn("100");
        Mockito.when(ctx.formParam("newCarprotWidth")).thenReturn("100");
        Mockito.when(ctx.formParam("newStatus")).thenReturn(OrderStatus.ORDER_ASSIGNED.toString());
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeUpdate()).thenReturn(0);

        OrderController.updateOrderWidthOutShed(ctx, connectionPool);

        InOrder inOrder = Mockito.inOrder(ctx);
        inOrder.verify(ctx).sessionAttribute("order");
        inOrder.verify(ctx).sessionAttribute("errorMessage", "Order did not exist");

    }
}

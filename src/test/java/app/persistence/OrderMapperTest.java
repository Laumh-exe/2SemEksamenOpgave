package app.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.valueOf;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;


import app.model.entities.Carport;
import app.model.entities.Shed;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import app.model.entities.Order;
import app.model.entities.OrderStatus;
import app.exceptions.OrderNotFoundException;

public class OrderMapperTest {

    private ConnectionPool connectionPool;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() throws SQLException {
        connectionPool = mock(ConnectionPool.class);      
    }

    @AfterEach
    public void tearDown(){
        connectionPool = null;
    }
    
    private void getAllTestSetup() throws SQLException {
        String sql = "SELECT * FROM public.order";
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

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

    
    @Test
    public void allOrdersTest() throws ParseException, SQLException{

      // arrange
        getAllTestSetup();
        ArrayList<Order> expected = new ArrayList<>();

        expected.add(new Order(1, sdf.parse("2023-12-20"), OrderStatus.READY_FOR_REVIEW, 11500d, new Carport(10d, 10d, new Shed(-1d, -1d))));
        expected.add(new Order(2, sdf.parse("2023-12-21"), OrderStatus.PRICE_PRESENTED, 100.1, new Carport(100d, 20d, new Shed(10d, 10d))));


        // act
        var actual = OrderMapper.getAllOrders(connectionPool);

        // assert
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertTrue(expected.get(i).equals(actual.get(i)));
        }
    }

    @Test
    public void updateOrderWithoutShedTest() throws SQLException, OrderNotFoundException{
        //Arrange
        Order order = new Order(1, 1, 1, Date.from(Instant.now()), OrderStatus.ORDER_ASSIGNED, 100d, new Carport(110d, 50d,null));
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width) = (?, ?, ?, ?) WHERE id = ?";
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeUpdate()).thenReturn(1);
        
        //Act
        OrderMapper.updateOrderWidthOutShed(order, connectionPool);
        
        //Assert
        InOrder inOrder = Mockito.inOrder(ps);
        inOrder.verify(ps).setString(1, order.getStatus().toString());
        inOrder.verify(ps).setDouble(2, order.getPrice());
        inOrder.verify(ps).setDouble(3, order.getCarport().getLength());
        inOrder.verify(ps).setDouble(4, order.getCarport().getWidth());
        inOrder.verify(ps).setInt(5, order.getId());

    }

    @Test
    public void updateOrderWidthoutShedNoOrdersUpdatedTest() throws SQLException{
        Order order = new Order(1, 1, 1, Date.from(Instant.now()), OrderStatus.ORDER_ASSIGNED, 100d, new Carport(110d, 50d,null));
        String sql = "UPDATE public.order SET (status, total_price, carport_length, carport_width) = (?, ?, ?, ?) WHERE id = ?";
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeUpdate()).thenReturn(0);
        
        assertThrows(OrderNotFoundException.class, () -> {OrderMapper.updateOrderWidthOutShed(order, connectionPool);});
    }

    
    
}

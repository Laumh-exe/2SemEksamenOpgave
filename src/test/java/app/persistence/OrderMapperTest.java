package app.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import app.entities.Order;
import app.entities.OrderStatus;
import app.persistence.ConnectionPool;

public class OrderMapperTest {

    private ConnectionPool connectionPool;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setup() throws SQLException{
        String sql = "SELECT * FROM orders";
        
        connectionPool = mock(ConnectionPool.class);
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        

        getAllTestSetup(sql, connection, ps, rs);
        
    }

    private void getAllTestSetup(String sql, Connection connection, PreparedStatement ps, ResultSet rs) throws SQLException {
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(rs.getInt("id")).thenReturn(1).thenReturn(2);
        Mockito.when(rs.getString("sataus")).thenReturn("READY_FOR_REVIEW").thenReturn("PRICE_PRESENTED");
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
    }
    
    @Test
    public void allOrdersTest() throws ParseException{
        // arrange
        ArrayList<Order> expected = new ArrayList<>();
        expected.add(new Order(1, sdf.parse("2023-12-20"), OrderStatus.READY_FOR_REVIEW, 11500d, 10d, 10d, -1d, -1d));
        expected.add(new Order(2, sdf.parse("2023-12-21"), OrderStatus.PRICE_PRESENTED, 100.1, 100d, 20d, 10d, 10d));

        // act
        var actual = OrderMapper.getAllOrders(connectionPool);

        // assert
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            
            
            assertTrue(expected.get(i).equals(actual.get(i)));
        }



    }
}

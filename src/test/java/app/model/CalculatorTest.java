package app.model;

import app.TestTemplate;
import app.model.entities.Carport;
import app.model.entities.Item;
import app.model.entities.Shed;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CalculatorTest {

    private ConnectionPool connectionPool;
    private Connection connection;
    private PreparedStatement ps;
    ResultSet rs;
    private Context ctx;
    private int carportLengthCM;
    private int carportWidthCM;
    private int shedWidthCM;
    private Carport carportWithShed;

    @BeforeEach
    public void setup() throws SQLException {
        connectionPool = mock(ConnectionPool.class);
        ctx = mock(Context.class);
        connection = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        getAllTestSetup();
        Carport carportWithoutShed = new Carport(7.8, 6);
        carportWithShed = new Carport(7.8, 6, new Shed(2, 6));
        carportLengthCM = (int) (carportWithShed.getLengthMeter() * 100);
        carportWidthCM = (int) (carportWithShed.getWidthMeter() * 100);
        int shedWidthCM = (int) (carportWithShed.getShed().getWidthMeter() * 100);
    }

    @AfterEach
    public void tearDown() {
        connectionPool = null;
    }

    private void getAllTestSetup() throws SQLException {
        String sql = "SELECT * FROM public.item";
        Mockito.when(connectionPool.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(sql)).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenReturn(rs);

            Mockito.when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
            Mockito.when(rs.getInt("id")).thenReturn(1, 2, 3,4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);
            Mockito.when(rs.getString("unit")).thenReturn("Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk","Stk");
            Mockito.when(rs.getString("description")).thenReturn( "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "45x195 mm. spærtræ - til rem", "97x97 mm. trykimp. Stolpe");
            Mockito.when(rs.getDouble("length")).thenReturn(240d, 300d, 360d, 420d, 480d, 540d, 600d, 660d, 720d, 240d, 300d, 360d, 420d, 480d, 540d, 600d, 660d, 720d, 300d);
            Mockito.when(rs.getDouble("price_pr_unit")).thenReturn(100d, 120d, 140d, 160d, 180d, 200d, 220d, 240d, 260d, 100d, 120d, 140d, 160d, 180d, 200d, 220d, 240d, 260d, 340d);
            Mockito.when(rs.getString("function")).thenReturn("spær", "spær", "spær", "spær", "spær", "spær", "spær", "spær", "spær", "rem", "rem", "rem", "rem", "rem", "rem", "rem", "rem", "rem", "stolpe");
    }


    /*
    @Test
    void calculateItemList() {
        Calculator calculator = Calculator.getInstance(connectionPool);
        // Arrange
        Item item = new Item(6,220,600,"stk","45x195 mm. spærtræ",2,"spær");
        String expected =
        String actual = null;

        // Act
        actual = TestTemplate.testString();

        // Assert
        assertEquals(expected,actual);
    }

     */

    @Test
    void calculateSpærWithShed() {
        Calculator calculator = Calculator.getInstance(connectionPool);
        ArrayList<Item> actual = new ArrayList<>();
        // Arrange
        ArrayList<Item> expected = new ArrayList<>();
        expected.add(new Item(7, 220, 600, "Stk", "45x195 mm. spærtræ", 15, "spær"));
        try {
            actual = calculator.calculateSpær(carportWithShed, carportLengthCM, carportWidthCM);
        } catch (Exception e) {
            e.getMessage();
        }

        assertEquals(expected, actual);
    }

    @Test
    void getStolpeQuantity() {
    }

    @Test
    void calculateRemWithShed() {
        Calculator calculator = Calculator.getInstance(connectionPool);
        ArrayList<Item> actual = new ArrayList<>();
        // Arrange
        ArrayList<Item> expected = new ArrayList<>();
        expected.add(new Item(6, 220, 600, "stk", "45x195 mm. spærtræ", 2, "spær"));
        expected.add(new Item(3, 140, 360, "stk", "45x195 mm. spærtræ", 1, "spær"));

    }
}
package app.model;

import app.persistence.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CalculatorTest {

    private Connectionpool connectionPool;
    @BeforeEach
    public void setup() throws SQLException {
        connectionPool = mock(ConnectionPool.class);
    }

    @AfterEach
    public void tearDown(){
        connectionPool = null;
    }


    @Test
    void calculateItemList() {
    }

    @Test
    void getSpærQuantity() {
    }

    @Test
    void getStolpeQuantity() {
    }

    @Test
    void getRemQuantity() {
    }
}
package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserMapper {


    public UserMapper(){

    }

    public void createUser(String name, String password, String rank, ConnectionPool connectionPool) throws SQLException
    {
        String sql= "INSERT INTO \"user\" (username, password, rank, balance) VALUES (?, ?, ?, 200)";
        try(Connection connection = connectionPool.getConnection()){
            try(PreparedStatement preparedStatement = connectionPool.getConnection().prepareStatement(sql)){
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, rank);            }

        }
    }
}

package cybercell;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10999999";
    private static final String USER = "sql10999999";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

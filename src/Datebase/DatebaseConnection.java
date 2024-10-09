package Datebase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatebaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/BookStore";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Tessy_Sammy28*";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

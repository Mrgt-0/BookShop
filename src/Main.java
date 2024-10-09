import BookStoreController.*;
import BookStoreModel.BookStore;
import BookStoreModel.BookStoreSerializable;
import BookStoreModel.Order;
import ConsoleUI.Builder;
import ConsoleUI.BuilderController;
import ConsoleUI.Navigator;
import DI.DependencyInjector;
import Datebase.*;
import Repository.BookRepository;
import Repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.*;
import Status.*;

public class Main {
    private static BookStore bookStore = DependencyInjector.getInstance(BookStore.class);

    public static void main(String[] args) {
        try {
            try (Connection connection = getConnection()) {
                connection.setAutoCommit(false);

                DatabaseInitializer databaseInitializer = new DatabaseInitializer();
                databaseInitializer.initializeDatabase();

                BookRepository bookRepository = new BookRepository();
                OrderRepository orderRepository = new OrderRepository();
                OrderController orderController = new OrderController(bookStore);
                BookStoreController bookStoreController = new BookStoreController(bookStore, orderController);

                bookStoreController.placeOrder("Война и мир");
                bookStoreController.placeOrder("Преступление и наказание");

                connection.commit();

                Navigator navigator = new Navigator();
                Builder builder = new Builder(bookStore);
                BuilderController builderController = new BuilderController(builder, navigator);
                builderController.run();

                BookStoreSerializable bookStoreSerializable = new BookStoreSerializable(bookStore);
                bookStoreSerializable.saveState();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String username = "your_username";
        String password = "your_password";

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get connection", e);
        }
    }
}
import BookStoreController.*;
import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.BookStoreSerializable;
import BookStoreModel.Order;
import ConsoleUI.Builder;
import ConsoleUI.BuilderController;
import ConsoleUI.Navigator;
import DI.DependencyInjector;
import DI.Inject;
import Datebase.*;
import Repository.BookRepository;
import Repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.*;

import Repository.RequestRepository;
import Status.*;

public class Main {
    public static void main(String[] args) {
        BookStore bookStore=new BookStore();

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            DatabaseInitializer databaseInitializer = new DatabaseInitializer();

            BookRepository bookRepository = DependencyInjector.getInstance(BookRepository.class);
            OrderRepository orderRepository = DependencyInjector.getInstance(OrderRepository.class);
            RequestRepository requestRepository = DependencyInjector.getInstance(RequestRepository.class);

            OrderController orderController = new OrderController(bookStore, orderRepository);
            BookStoreController bookStoreController = new BookStoreController(bookStore, orderController, bookRepository, requestRepository);

            bookStoreController.placeOrder("Война и мир");
            bookStoreController.placeOrder("Преступление и наказание");

            connection.commit();

            Navigator navigator = new Navigator();
            Builder builder = new Builder(bookStore);
            BuilderController builderController = new BuilderController(builder, navigator);
            builderController.run();

            BookStoreSerializable bookStoreSerializable = new BookStoreSerializable(bookStore);
            bookStoreSerializable.saveState();
        } catch (SQLException | ExceptionInInitializerError e) {
            throw new RuntimeException("SQL exception: ", e);
        }
    }
}
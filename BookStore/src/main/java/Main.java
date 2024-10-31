import BookStoreController.*;
import BookStoreModel.BookStore;
import BookStoreModel.BookStoreSerializable;
import ConsoleUI.Builder;
import ConsoleUI.BuilderController;
import ConsoleUI.Navigator;
import DI.DependencyInjector;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;

import javax.transaction.SystemException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SystemException {
        BookStore bookStore=new BookStore();

            BookRepository bookRepository = DependencyInjector.getInstance(BookRepository.class);
            OrderRepository orderRepository = DependencyInjector.getInstance(OrderRepository.class);
            RequestRepository requestRepository = DependencyInjector.getInstance(RequestRepository.class);

            OrderController orderController = new OrderController(bookStore);
            BookStoreController bookStoreController = new BookStoreController(bookStore);

            bookStoreController.placeOrder("Война и мир");
            bookStoreController.placeOrder("Преступление и наказание");


            Navigator navigator = new Navigator();
            Builder builder = new Builder(bookStore);
            BuilderController builderController = new BuilderController(builder, navigator);
            builderController.run();

            BookStoreSerializable bookStoreSerializable = new BookStoreSerializable(bookStore);
            bookStoreSerializable.saveState();
    }
}
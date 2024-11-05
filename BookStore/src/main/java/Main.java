import BookStoreController.*;
import BookStoreModel.BookStore;
import BookStoreModel.BookStoreSerializable;
import ConsoleUI.Builder;
import ConsoleUI.BuilderController;
import ConsoleUI.Navigator;
import DI.AppConfig;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.transaction.SystemException;

public class Main {
    public static void main(String[] args) throws SystemException {
        ApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class);
        BookStore bookStore=new BookStore();

            BookRepository bookRepository = context.getBean(BookRepository.class);
            OrderRepository orderRepository = context.getBean(OrderRepository.class);
            RequestRepository requestRepository = context.getBean(RequestRepository.class);

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
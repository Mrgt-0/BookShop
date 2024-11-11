import BookStoreService.*;
import BookStoreModel.BookStore;
import BookStoreModel.BookStoreSerializable;
import ConsoleUI.Builder;
import ConsoleUI.BuilderService;
import ConsoleUI.Navigator;
import DI.AppConfig;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.transaction.SystemException;

public class Main {
    public static void main(String[] args) throws SystemException {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

            BookStore bookStore=context.getBean(BookStore.class);

            BookRepository bookRepository = context.getBean(BookRepository.class);
            OrderRepository orderRepository = context.getBean(OrderRepository.class);
            RequestRepository requestRepository = context.getBean(RequestRepository.class);

            BookStoreService bookStoreService = new BookStoreService(bookStore);

            bookStoreService.placeOrder("Война и мир");
            bookStoreService.placeOrder("Преступление и наказание");


            Navigator navigator = new Navigator();
            Builder builder = new Builder(bookStore);
            BuilderService builderService = new BuilderService(builder, navigator);
            builderService.run();

            BookStoreSerializable bookStoreSerializable = new BookStoreSerializable(bookStore);
            bookStoreSerializable.saveState();
        }catch (BeanInstantiationException | BeanCreationException e){
            e.getMessage();
        }
    }
}
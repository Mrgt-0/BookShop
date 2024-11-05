package DI;

import BookStoreController.*;
import BookStoreModel.*;
import Repository.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.activation.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/BookStore");
        dataSource.setUser("root");
        dataSource.setPassword("Tessy_Sammy28*");
        return (DataSource) dataSource;
    }

    @Bean
    public BookStore bookStore(){
        return bookStore();
    }

    @Bean
    public BookRepository bookRepository(){
        return new BookRepository();
    }

    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepository();
    }

    @Bean
    public RequestRepository requestRepository(){
        return new RequestRepository();
    }

    @Bean
    public RequestController requestController(){
        return new RequestController(bookStore());
    }

    @Bean
    public OrderController orderController(){
        return new OrderController(bookStore());
    }

    @Bean
    public BookStoreController bookStoreController() {
        return new BookStoreController(bookStore());
    }

    @Bean
    public BookStoreSerializable bookStoreSerializable(){
        return  new BookStoreSerializable(bookStore());
    }
}

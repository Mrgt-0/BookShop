package BookStoreController;
import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Repository.OrderRepository;
import Status.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

@Singleton
public class OrderController {

    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;

    @Inject
    private OrderRepository orderRepository;

    private static final Logger logger = LogManager.getLogger(BookStoreController.class);

    public OrderController(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void createOrder(Book book, OrderStatus status) throws SystemException {
        Order order=new Order(book, status);
        orderRepository.save(order);
    }

    public void updateStatus(Order order, OrderStatus status) throws SystemException {
        orderRepository.update(order);
    }

    public void cancelOrder(Order order) throws SystemException {
        orderRepository.delete(order.getOrderId());
    }
}

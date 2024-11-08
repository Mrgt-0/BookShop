package BookStoreService;
import BookStoreModel.*;
import Repository.OrderRepository;
import Status.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.SystemException;

@Service
public class OrderService {
    @Autowired
    private BookStoreSerializable bookStoreSerializable;
    @Autowired
    private BookStore bookStore;
    @Autowired
    private OrderRepository orderRepository;
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    public OrderService(BookStore bookStore){
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

package BookStoreController;
import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Repository.OrderRepository;
import Status.*;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

@Singleton
public class OrderController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderRepository orderRepository;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(OrderController.class);

    public OrderController(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void createOrder(Book book, OrderStatus status) {
        if (book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, status);
            bookStore.getOrders().add(order);
            orderRepository.save(order);
            logger.info("Заказ на книгу {} создан.");
            bookStoreSerializable.saveState();
        } else {
            logger.warning("Книга {} недоступна для заказа.");
        }
    }

    public void updateStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        orderRepository.update(order);
        System.out.println("Статус заказа на книгу " + order.getBook().getTitle() + " изменен на " + status + ".");
        bookStoreSerializable.saveState();
    }

    public void cancelOrder(Order order) {
        orderRepository.delete(order.getOrderId());
        bookStore.getOrders().remove(order);
        System.out.println("Заказ на книгу " + order.getBook().getTitle() + " отменен.");
        bookStoreSerializable.saveState();
    }
}

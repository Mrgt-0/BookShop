package BookStoreController;
import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Repository.OrderRepository;
import Status.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

@Singleton
public class OrderController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(BookStoreController.class);

    public OrderController(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void createOrder(Book book, OrderStatus status) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            if (book.getStatus() == BookStatus.IN_STOCK) {
                Order order = new Order(book, status);
                bookStore.getOrders().add(order);
                orderRepository.save(order);
                logger.info("Заказ на книгу '{}' создан.", book.getTitle());
            } else {
                logger.warn("Книга '{}' недоступна для заказа.", book.getTitle());
            }
            bookStoreSerializable.saveState();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при создании заказа на книгу '{}': {}", book.getTitle(), e.getMessage(), e);
        }
    }

    public void updateStatus(Order order, OrderStatus status) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            order.setStatus(status);
            orderRepository.update(order);
            logger.info("Статус заказа на книгу '{}' изменен на '{}'.", order.getBook().getTitle(), status);
            bookStoreSerializable.saveState();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при обновлении статуса заказа: {}", e.getMessage(), e);
        }
    }

    public void cancelOrder(Order order) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            orderRepository.delete(order.getOrderId());
            bookStore.getOrders().remove(order);
            logger.info("Заказ на книгу '{}' отменен.", order.getBook().getTitle());
            bookStoreSerializable.saveState();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при отмене заказа на книгу '{}': {}", order.getBook().getTitle(), e.getMessage(), e);
        }
    }
}

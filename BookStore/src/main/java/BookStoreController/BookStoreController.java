package BookStoreController;

import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Property.Util;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;

import java.time.LocalDate;
import java.util.Optional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

@Singleton
@Controller
public class BookStoreController {
    @Autowired
    private BookStoreSerializable bookStoreSerializable;
    @Autowired
    private BookStore bookStore;
    @Autowired
    private OrderController orderController;
    @Autowired
    private RequestController requestController;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RequestRepository requestRepository;

    private static final Logger logger = LogManager.getLogger(BookStoreController.class);

    public BookStoreController(BookStore bookStore){
        this.bookStore=bookStore;
        bookStoreSerializable=new BookStoreSerializable(bookStore);

    }

    public void addBook(Book book) throws SystemException {
        bookRepository.save(book);
    }

    public void removeBook(int bookId) throws SystemException {
        bookRepository.delete(bookId);
    }

    public void updateOrderStatus(int bookId, OrderStatus status) {
        orderRepository.updateOrderStatus(bookId, status);
    }

    public void cancelOrder(String bookTitle) {
        try {
            bookStore.getOrders().stream()
                    .filter(order -> order.getBook().getTitle().equals(bookTitle))
                    .findFirst()
                    .ifPresent(order -> {
                        try {
                            orderController.cancelOrder(order);
                            orderRepository.delete(order.getOrderId());
                            logger.info("Заказ для книги '{}' отменен.", bookTitle);
                        } catch (SystemException e) {
                            logger.error("Ошибка при отмене заказа для книги '{}': {}", bookTitle, e.getMessage(), e);
                        }
                    });
        } catch (Exception e) {
            logger.error("Ошибка при отмене заказа для книги '{}': {}", bookTitle, e.getMessage(), e);
        }
    }

    public void placeOrder(String title) throws SystemException {
        Order order = orderRepository.placeOrder(title, bookStore);

        if (order != null) {
            bookStore.getOrders().add(order);
            logger.info("Заказ добавлен в книжный магазин.");
        } else {
            logger.warn("Не удалось добавить заказ в книжный магазин.");
        }
    }

    public void fulfillOrder(String title) throws SystemException {
        orderRepository.fulfillOrderByTitle(title);
    }

    private void fulfillPendingRequests(Book book) throws SystemException {
        RequestController requestController = new RequestController(bookStore);
        requestController.fulfillPendingRequests(book);
        if (Util.isMarkOrdersAsCompleted()) {
            fulfillOrder(book.getTitle());
        }
    }

    private void handleBookRemoval(Book book, Object identifier) {
        if (book != null) {
            bookRepository.delete(book.getBookId());
            bookStore.getBookInventory().remove(book.getTitle());
            logger.info("Книга с идентификатором '{}' удалена.", identifier);
        } else {
            logger.warn("Книга с идентификатором '{}' не найдена.", identifier);
        }
    }
}

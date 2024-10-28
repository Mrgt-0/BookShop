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

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

@Singleton
public class BookStoreController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderController orderController;
    @Inject
    private RequestController requestController;
    @Inject
    private BookRepository bookRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private RequestRepository requestRepository;

    private static final Logger logger = LogManager.getLogger(BookStoreController.class);

    @Inject
    private EntityManager entityManager;

    public BookStoreController(BookStore bookStore){
        this.bookStore=bookStore;
        bookStoreSerializable=new BookStoreSerializable(bookStore);
    }

    public void addBook(Book book) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            bookRepository.save(book);
            bookStore.getBookInventory().put(book.getTitle(), book);

            fulfillPendingRequests(book);

            transaction.commit();
            logger.info("Книга '{}' добавлена.", book.getTitle());
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при добавлении книги: {}", e.getMessage(), e);
        }
    }

    public void removeBook(Object bookIdentifier) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            if (bookIdentifier instanceof Integer) {
                int bookId = (Integer) bookIdentifier;
                logger.debug("Попытка удалить книгу с ID: {}", bookId);
                Book book = bookRepository.getById(bookId);
                handleBookRemoval(book, bookId);
            } else if (bookIdentifier instanceof String) {
                String bookTitle = (String) bookIdentifier;
                logger.debug("Попытка удалить книгу с названием: {}", bookTitle);
                Book book = bookRepository.getByTitle(bookTitle);
                handleBookRemoval(book, bookTitle);
            } else {
                logger.error("Неверный тип идентификатора. Ожидалось: Integer или String.");
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при удалении книги: {}", e.getMessage(), e);
        }
    }

    public void updateOrderStatus(String bookTitle, OrderStatus status) {
        if (Util.isMarkOrdersAsCompleted()) {
            try {
                bookStore.getOrders().stream()
                        .filter(order -> order.getBook().getTitle().equals(bookTitle))
                        .findFirst()
                        .ifPresent(order -> {
                            try {
                                orderController.updateStatus(order, status);
                            } catch (SystemException e) {
                                throw new RuntimeException(e);
                            }
                            orderRepository.updateOrderStatus(order.getOrderId(), status);
                        });
                logger.info("Статус заказа для книги '{}' обновлен на '{}'.", bookTitle, status);
            } catch (Exception e) {
                logger.error("Ошибка при обновлении статуса заказа: {}", e.getMessage(), e);
            }
        } else {
            System.out.println("Обновление статуса заказа отключено.");
        }
    }

    public void cancelOrder(String bookTitle) {
        try {
            bookStore.getOrders().stream()
                    .filter(order -> order.getBook().getTitle().equals(bookTitle))
                    .findFirst()
                    .ifPresent(order -> {
                        try {
                            orderController.cancelOrder(order);
                        } catch (SystemException e) {
                            throw new RuntimeException(e);
                        }
                        orderRepository.delete(order.getOrderId());
                    });
            logger.info("Заказ для книги '{}' отменен.", bookTitle);
        } catch (Exception e) {
            logger.error("Ошибка при отмене заказа для книги '{}': {}", bookTitle, e.getMessage(), e);
        }
    }

    public void placeOrder(String title) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            Book book = bookStore.getBookInventory().get(title);

            if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
                Order order = new Order(book, OrderStatus.NEW);
                bookStore.getOrders().add(order);
                orderRepository.save(order);
                logger.info("Заказ на книгу '{}' был успешно размещен.", book.getTitle());
            } else if (book != null) {
                logger.warn("Книги '{}' нет на складе. Запрос на эту книгу оставлен.", book.getTitle());
                RequestController bookRequest = new RequestController(bookStore);
                bookRequest.requestBook(bookStore, title);
            } else {
                logger.error("Книга с названием '{}' не найдена.", title);
            }

            bookStoreSerializable.saveState();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при размещении заказа на книгу '{}': {}", title, e.getMessage(), e);
        }
    }

    public void fulfillOrder(String title) throws SystemException {
        Transaction transaction = (Transaction) entityManager.getTransaction();
        try {
            ((EntityTransaction) transaction).begin();
            Optional<Order> orderOptional = bookStore.getOrders().stream()
                    .filter(order -> order.getBook().getTitle().equals(title) && order.getStatus() == OrderStatus.NEW)
                    .findFirst();

            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (Util.isMarkOrdersAsCompleted()) {
                    orderController.updateStatus(order, OrderStatus.FULFILLED);
                    order.setExecutionDate(LocalDate.now());
                    bookStore.setTotalEarnings(order.getBook().getPrice());
                    bookStore.setTotalOrdersFulfilled(1);
                    orderRepository.update(order);
                    logger.info("Заказ на книгу '{}' успешно выполнен.", order.getBook().getTitle());
                } else {
                    logger.info("Заказ на книгу '{}' ожидает выполнения.", order.getBook().getTitle());
                }
                bookStoreSerializable.saveState();
            } else {
                logger.warn("Заказ на книгу '{}' не найден или уже выполнен.", title);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при выполнении заказа на книгу '{}': {}", title, e.getMessage(), e);
        }
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

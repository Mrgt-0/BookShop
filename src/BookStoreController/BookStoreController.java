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

@Singleton
public class BookStoreController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderController orderController;
    @Inject
    private BookRepository bookRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private RequestRepository requestRepository;

    private static final Logger logger = LogManager.getLogger(BookStoreController.class);

    public BookStoreController(BookStore bookStore){
        this.bookStore=bookStore;
        bookStoreSerializable=new BookStoreSerializable(bookStore);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
        bookStore.getBookInventory().put(book.getTitle(), book);

        RequestController requestController = new RequestController(bookStore, orderRepository);
        requestController.fulfillPendingRequests(bookStore);

        if (Util.isMarkOrdersAsCompleted())
            fulfillOrder(book.getTitle());

        bookStoreSerializable.saveState();
    }

    public void removeBook(Object bookIdentifier) {
        if (bookIdentifier instanceof Integer) {
            int bookId = (Integer) bookIdentifier;
            logger.debug("Attempting to remove book with ID: {}", bookId);

            Book book = bookRepository.getById(bookId);
            if (book != null) {
                bookRepository.delete(bookId);
                bookStore.getBookInventory().remove(book.getTitle());
                bookStoreSerializable.saveState();
                logger.info("Книга с ID {} удалена.", bookId);
            } else {
                logger.warn("Книга с ID {} не найдена.", bookId);
            }

        } else if (bookIdentifier instanceof String) {
            String bookTitle = (String) bookIdentifier;
            logger.debug("Попытка удалить книгу с названием: {}", bookTitle);

            Book book = bookRepository.getByTitle(bookTitle);
            if (book != null) {
                bookRepository.delete(book.getBookId());
                bookStore.getBookInventory().remove(book.getTitle());
                bookStoreSerializable.saveState();
                logger.info("Книга с названием '{}' удалена.", bookTitle);
            } else
                logger.warn("Книга с названием '{}' не найдена.", bookTitle);
        } else
            logger.error("Неверный тип идентификатора. Ожидалось: Integer или String.");
        }

    public void updateOrderStatus(String bookTitle, OrderStatus status) {
        if(Util.isMarkOrdersAsCompleted()) {
            bookStore.getOrders().stream()
                    .filter(order -> order.getBook().getTitle().equals(bookTitle))
                    .findFirst()
                    .ifPresent(order -> {
                        orderController.updateStatus(order, status);
                        orderRepository.updateOrderStatus(order.getOrderId(), status);
                    });
            bookStoreSerializable.saveState();
        }else
            System.out.println("Обновление статуса заказа отключено.");
    }

    public void cancelOrder(String bookTitle) {
        bookStore.getOrders().stream()
                .filter(order -> order.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(order -> {
                    orderController.cancelOrder(order);
                    orderRepository.delete(order.getOrderId());
                });
        bookStoreSerializable.saveState();
    }

    public void placeOrder(String title) {
        Book book = bookStore.getBookInventory().get(title);
        if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, OrderStatus.NEW);
            bookStore.getOrders().add(order);
            orderRepository.save(order);
            System.out.println("Заказ на книгу: " + book.getTitle());
            bookStoreSerializable.saveState();
        } else if (book != null) {
            System.out.println("Книги: " + book.getTitle() + " нет на складе. Запрос на эту книгу оставлен");
            RequestController bookRequest = new RequestController(bookStore, orderRepository);
            bookRequest.requestBook(bookStore, title);
            bookStoreSerializable.saveState();
        } else {
            System.out.println("Книга с таким названием не найдена");
        }
    }

    public void fulfillOrder(String title){
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
            } else
                System.out.println("Заказ на книгу " + order.getBook().getTitle() + " ожидает выполнения.");
            bookStoreSerializable.saveState();
        }
    }
}

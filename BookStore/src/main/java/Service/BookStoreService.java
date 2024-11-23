package Service;

import Model.*;
import DTO.OrderDTO;
import Property.Util;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.SystemException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookStoreService {
    @Autowired
    private BookStoreSerializable bookStoreSerializable;
    @Autowired
    private BookStore bookStore;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RequestRepository requestRepository;
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    public BookStoreService(BookStore bookStore){
        this.bookStore=bookStore;
        bookStoreSerializable=new BookStoreSerializable(bookStore);
    }

    public void addBook(Book book) throws SystemException {
        try {
            bookRepository.save(book);
            logger.info("Книга '{}' добавлена.", book.getTitle());
        } catch (Exception e) {
            logger.error("Ошибка при добавлении книги: {}", e.getMessage());
            throw new SystemException("Ошибка при добавлении книги.");
        }
    }

    public void removeBook(int bookId) throws SystemException {
        Optional<Optional<Book>> bookOptional = Optional.ofNullable(bookRepository.getById(bookId));
        if (bookOptional.isPresent()) {
            Optional<Book> book = bookOptional.get();
            bookRepository.delete(book);
            logger.info("Книга '{}' удалена.", book.get().getTitle());
        } else {
            logger.warn("Книга с ID '{}' не найдена.", bookId);
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus status) throws SystemException {
        try {
            orderRepository.updateOrderStatus(orderId, status);
            logger.info("Статус заказа с ID '{}' обновлен на '{}'.", orderId, status);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении статуса заказа: {}", e.getMessage());
            throw new SystemException("Ошибка при обновлении статуса заказа.");
        }
    }

    public void cancelOrder(String bookTitle) {
        try {
            bookStore.getOrders().stream()
                    .filter(order -> order.getBook().getTitle().equals(bookTitle))
                    .findFirst()
                    .ifPresent(order -> {
                        try {
                            orderService.cancelOrder(order);
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

    public void placeOrder(OrderDTO orderDTO) throws SystemException {
        try {
            Book book = bookRepository.getByTitle(orderDTO.getBook().getTitle())
                    .orElseThrow(() -> new SystemException("Книга '" + orderDTO.getBook().getTitle() + "' не найдена."));

            Order order = new Order(book, orderDTO.getStatus());
            order.setOrderPrice(orderDTO.getOrderPrice());
            order.setExecutionDate(orderDTO.getExecutionDate());
            orderRepository.save(order);
            logger.info("Заказ для книги '{}' был создан.", orderDTO.getBook().getTitle());
        } catch (Exception e) {
            logger.error("Ошибка при создании заказа для книги '{}': {}", orderDTO.getBook().getTitle(), e.getMessage());
            throw new SystemException("Ошибка при создании заказа для книги.");
        }
    }

    public void fulfillOrder(String title) throws SystemException {
        try {
            orderRepository.fulfillOrderByTitle(title);
            logger.info("Заказ для книги '{}' был выполнен.", title);
        } catch (Exception e) {
            logger.error("Ошибка при выполнении заказа для книги '{}': {}", title, e.getMessage());
            throw new SystemException("Ошибка при выполнении заказа.");
        }
    }

    private void fulfillPendingRequests(Book book) throws SystemException {
        requestService.fulfillPendingRequests(book);
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

    public List<Book> listBooks() throws SystemException {
        try {
            List<Book> books = bookRepository.getAll(); // Метод, который возвращает все книги
            return books;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка книг: {}", e.getMessage());
            throw new SystemException("Ошибка при получении списка книг.");
        }
    }
}

package com.books.BookStore.example.Service;

import com.books.BookStore.example.DTO.BookDTO;
import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Model.BookStore;
import com.books.BookStore.example.Model.BookStoreSerializable;
import com.books.BookStore.example.Model.Order;
import com.books.BookStore.example.Property.Util;
import com.books.BookStore.example.Repository.BookRepository;
import com.books.BookStore.example.Repository.OrderRepository;
import com.books.BookStore.example.Repository.RequestRepository;
import com.books.BookStore.example.Status.OrderStatus;
import jakarta.transaction.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<BookDTO> listBooks() throws SystemException {
        try {
            List<Book> books = bookRepository.getAll();
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new SystemException();
        }
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getStatus(),
                book.getPublishDate(),
                book.getPrice(),
                book.getDescription()
        );
    }

    public void addBook(BookDTO bookDTO) throws SystemException {
        try {
            Book book = new Book(
                    bookDTO.getTitle(),
                    bookDTO.getAuthor(),
                    bookDTO.getStatus(),
                    bookDTO.getPublishDate(),
                    bookDTO.getPrice(),
                    bookDTO.getDescription()
            );
            bookRepository.save(book);
        } catch (Exception e) {
            throw new SystemException();
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
                            orderService.cancelOrder(order.getOrderId());
                        } catch (SystemException e) {
                            throw new RuntimeException(e);
                        }
                        orderRepository.delete(order.getOrderId());
                        logger.info("Заказ для книги '{}' отменен.", bookTitle);
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
        requestService.fulfillPendingRequests();
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

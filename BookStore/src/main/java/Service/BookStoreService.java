package com.books.BookStore.example.Service;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.SystemException;
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
            throw new SystemException("Ошибка при добавлении книги", e);
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

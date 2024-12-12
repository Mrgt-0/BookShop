package com.books.BookStore.example.Service;

import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Model.BookStore;
import com.books.BookStore.example.Model.BookStoreSerializable;
import com.books.BookStore.example.Model.Order;
import com.books.BookStore.example.Repository.BookRepository;
import com.books.BookStore.example.Repository.OrderRepository;
import com.books.BookStore.example.Status.OrderStatus;
import jakarta.transaction.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {
    @Autowired
    private BookStoreSerializable bookStoreSerializable;
    @Autowired
    private BookStore bookStore;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    public OrderService(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void createOrder(OrderDTO orderDTO) throws SystemException {
        Book book = bookRepository.getByTitle(orderDTO.getBook().getTitle())
                .orElseThrow(() -> new SystemException("Книга не найдена."));

        Order order = new Order(book, orderDTO.getStatus());
        orderRepository.save(order);
    }

    public void updateStatus(int orderId, OrderStatus status) throws SystemException {
        Order order = orderRepository.getById(orderId)
                .orElseThrow(() -> new SystemException("Заказ не найден."));

        order.setStatus(status);
        orderRepository.save(order);
    }

    public void cancelOrder(int orderId) throws SystemException {
        Order order = orderRepository.getById(orderId)
                .orElseThrow(() -> new SystemException("Заказ не найден."));

        orderRepository.delete(order.getOrderId());
    }
}
package com.books.BookStore.example.Controller;


import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Model.Order;
import com.books.BookStore.example.Repository.BookRepository;
import com.books.BookStore.example.Repository.OrderRepository;
import com.books.BookStore.example.Service.OrderService;
import com.books.BookStore.example.Status.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.SystemException;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) throws SystemException {
        Book book = bookRepository.getByTitle(orderDTO.getBook().getTitle())
                .orElseThrow(() -> new SystemException("Книга не найдена."));
        orderService.createOrder(book, orderDTO.getStatus());
        return ResponseEntity.ok("Заказ успешно создан.");
    }

    @PatchMapping("/update/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable int orderId, @RequestBody OrderStatus status) throws SystemException {
        Order order = orderRepository.getById(orderId)
                .orElseThrow(() -> new SystemException("Заказ не найден."));
        orderService.updateStatus(order, status);
        return ResponseEntity.ok("Статус заказа успешно обновлен.");
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId) throws SystemException {
        Order order = orderRepository.getById(orderId)
                .orElseThrow(() -> new SystemException("Заказ не найден."));
        orderService.cancelOrder(order);
        return ResponseEntity.ok("Заказ успешно отменен.");
    }


}

package com.books.BookStore.example.Controller;


import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Repository.*;
import com.books.BookStore.example.Service.OrderService;
import com.books.BookStore.example.Status.OrderStatus;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    public OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) throws SystemException {
        try {
            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("Заказ успешно создан.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при создании заказа.");
        }
    }

    @PatchMapping("/update/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable int orderId, @RequestBody OrderStatus status) throws SystemException {
        try {
            orderService.updateStatus(orderId, status);
            return ResponseEntity.ok("Статус заказа успешно обновлен.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при обновлении статуса заказа.");
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId) throws SystemException {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Заказ успешно отменен.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при отмене заказа.");
        }
    }
}

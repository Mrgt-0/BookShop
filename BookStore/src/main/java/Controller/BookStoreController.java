package com.books.BookStore.example.Controller;

import com.books.BookStore.example.DTO.BookDTO;
import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Status.OrderStatus;
import com.books.BookStore.example.Service.BookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.SystemException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookStoreController {
    @Autowired
    BookStoreService bookStoreService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() throws SystemException { 
        List<BookDTO> books = bookStoreService.listBooks(); 
        return ResponseEntity.ok(books); 
    }
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) {
        try { 
            bookStoreService.addBook(bookDTO); 
            return ResponseEntity.ok("Книга добавлена успешно."); 
        } catch (SystemException e) { 
            return ResponseEntity.status(500).body("Ошибка при добавлении книги."); 
        } 
    }
    @DeleteMapping("/bookId")
    public ResponseEntity<String> removeBook(@PathVariable int bookId) {
        try {
            bookStoreService.removeBook(bookId);
            return ResponseEntity.ok("Книга удалена успешно.");
        } catch (SystemException e) {
            return ResponseEntity.status(500).body("Ошибка при удалении книги.");
        }
    }
    @PatchMapping("/{bookId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int bookId, @RequestParam OrderStatus status) throws SystemException {
        bookStoreService.updateOrderStatus(bookId, status);
        return ResponseEntity.ok("Статус заказа успешно обновлён.");
    }
    @DeleteMapping("/{title}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable String title) {
        bookStoreService.cancelOrder(title);
        return ResponseEntity.ok("Заказ успешно отменён.");
    }
    @PostMapping("/{title}/order")
    public ResponseEntity<String> placeOrder(@PathVariable OrderDTO orderDTO) {
        try {
            bookStoreService.placeOrder(orderDTO);
            return ResponseEntity.status(201).body("Заказ успешно размещен.");
        } catch (SystemException e) {
            return ResponseEntity.status(500).body("Ошибка при размещении заказа.");
        }
    }
    @PostMapping("/{title}/fulfill")
    public ResponseEntity<String> fulfillOrder(@PathVariable String title) {
        try {
            bookStoreService.fulfillOrder(title);
            return ResponseEntity.ok("Заказ успешно выполнен.");
        } catch (SystemException e) {
            return ResponseEntity.status(500).body("Ошибка при выполнении заказа.");
        }
    }
}

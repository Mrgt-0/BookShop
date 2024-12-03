package com.books.BookStore.example.Controller;

import com.books.BookStore.example.DTO.RequestDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Model.Request;
import com.books.BookStore.example.Repository.RequestRepository;
import com.books.BookStore.example.Service.RequestService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/request")
    public ResponseEntity<String> requestBook(@RequestBody RequestDTO requestDTO) {
        try {
            requestService.requestBook(requestDTO.getBook().getTitle());
            return ResponseEntity.ok("Запрос на книгу успешно оставлен.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при запросе книги.");
        }
    }

    @GetMapping("/fulfill/{requestId}")
    public ResponseEntity<String> fulfillRequest(@PathVariable int requestId) {
        try {
            requestService.fulfillRequest(requestId);
            return ResponseEntity.ok("Запрос на книгу выполнен.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при выполнении запроса на книгу.");
        }
    }

    @PutMapping("/fulfill-pending")
    public ResponseEntity<String> fulfillPendingRequests(@RequestBody Book book) {
        try {
            requestService.fulfillPendingRequests();
            return ResponseEntity.ok("Все ожидающие запросы выполнены.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при выполнении ожидающих запросов.");
        }
    }
}

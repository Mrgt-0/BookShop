package BookStoreService;

import BookStoreModel.*;
import DI.Singleton;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.transaction.SystemException;
import java.util.List;

@Singleton
@Service
public class RequestService {
    @Autowired
    private BookStoreSerializable bookStoreSerializable;
    @Autowired
    private BookStore bookStore;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RequestRepository requestRepository;

    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    public RequestService(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void requestBook(BookStore bookStore, String bookTitle) throws SystemException {
        Book book = bookStore.getBookInventory().get(bookTitle);
        if (book != null) {
            if (book.getStatus() == BookStatus.OUT_OF_STOCK) {
                Request request = new Request(book);
                requestRepository.create(request);
                logger.info("Запрос на книгу '{}' оставлен.", book.getTitle());
            } else {
                OrderService orderController = new OrderService(bookStore);
                orderController.createOrder(book, OrderStatus.NEW);
                logger.info("Заказ на книгу '{}' создан.", book.getTitle());
            }
            bookStoreSerializable.saveState();
        } else
            logger.warn("Книга с названием '{}' не найдена в инвентаре.", bookTitle);
    }

    public void fulfillRequest(Request request) throws SystemException {
        if (request.getBook().getStatus() == BookStatus.IN_STOCK) {
            OrderService orderController = new OrderService(bookStore);
            orderController.createOrder(request.getBook(), OrderStatus.NEW);
            requestRepository.delete(request.getRequestId());
            logger.info("Запрос на книгу '{}' выполнен.", request.getBook().getTitle());
        } else
            logger.warn("Книга '{}' недоступна для заказа.", request.getBook().getTitle());
    }

    public void fulfillPendingRequests(Book book) throws SystemException {
        List<Request> requests = requestRepository.getAll();
        for (Request request : requests) {
            fulfillRequest(request);
        }
    }
}

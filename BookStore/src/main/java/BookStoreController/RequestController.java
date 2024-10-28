package BookStoreController;

import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class RequestController{
    private final BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private RequestRepository requestRepository;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RequestController.class);

    public RequestController(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void requestBook(BookStore bookStore, String bookTitle) {
        Book book = bookStore.getBookInventory().get(bookTitle);
        if (book != null) {
            if (book.getStatus() == BookStatus.OUT_OF_STOCK) {
                Request request = new Request(book);
                requestRepository.create(request);
                logger.info("Запрос на книгу {} оставлен.");
            } else {
                OrderController orderController = new OrderController(bookStore);
                orderController.createOrder(book, OrderStatus.NEW);
                logger.info("Заказ на книгу {} создан.");
            }
            bookStoreSerializable.saveState();
        } else
            logger.log(Level.parse("Книга с названием {} не найдена в инвентаре."), bookTitle);
    }

    public void fulfillRequest(Request request) {
        if (request.getBook().getStatus() == BookStatus.IN_STOCK) {
            OrderController orderController = new OrderController(bookStore);
            orderController.createOrder(request.getBook(), OrderStatus.NEW);
            requestRepository.delete(request.getRequestId());
            System.out.println("Запрос на книгу " + request.getBook().getTitle() + " выполнен.");
        } else
            System.out.println("Книга " + request.getBook().getTitle() + " недоступна для заказа.");
    }

    public void fulfillPendingRequests(BookStore bookStore) {
        List<Request> requests = requestRepository.getAll();
        for (Request request : requests) {
            fulfillRequest(request);
        }
    }
}

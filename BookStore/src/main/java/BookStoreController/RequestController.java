package BookStoreController;

import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;

import java.util.List;

@Singleton
public class RequestController{
    private final BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private RequestRepository requestRepository;

    public RequestController(BookStore bookStore, OrderRepository orderRepository){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
        this.requestRepository=requestRepository;
        this.orderRepository=orderRepository;
    }

    public void requestBook(BookStore bookStore, String bookTitle) {
        Book book = bookStore.getBookInventory().get(bookTitle);
        if (book != null) {
            if (book.getStatus() == BookStatus.OUT_OF_STOCK) {
                Request request = new Request(book);
                requestRepository.create(request);
                System.out.println("Запрос на книгу " + bookTitle + " оставлен.");
            } else {
                OrderController orderController = new OrderController(bookStore, orderRepository);
                orderController.createOrder(book, OrderStatus.NEW);
                System.out.println("Заказ на книгу " + bookTitle + " создан.");
            }
            bookStoreSerializable.saveState();
        } else
            System.out.println("Книга с названием " + bookTitle + " не найдена в инвентаре.");
    }

    public void fulfillRequest(Request request) {
        if (request.getBook().getStatus() == BookStatus.IN_STOCK) {
            OrderController orderController = new OrderController(bookStore, orderRepository);
            orderController.createOrder(request.getBook(), OrderStatus.NEW);
            requestRepository.delete(request.getRequestId());
            System.out.println("Запрос на книгу " + request.getBook().getTitle() + " выполнен.");
        } else {
            System.out.println("Книга " + request.getBook().getTitle() + " недоступна для заказа.");
        }
    }

    public void fulfillPendingRequests(BookStore bookStore) {
        List<Request> requests = requestRepository.getAll();
        for (Request request : requests) {
            fulfillRequest(request);
        }
    }
}

package BookStoreController;

import java.util.ArrayList;
import java.util.List;
import BookStoreModel.*;
import Status.*;

public class RequestController{
    private static Request request;
    public RequestController(Book book){
        request=new Request(book);
    }
    public void requestBook(BookStore bookStore, String bookTitle) {
        Book book = bookStore.getBookInventory().get(bookTitle);
        if (book != null) {
            if (this.request.getBook().getStatus() == BookStatus.OUT_OF_STOCK) {
                Request request = new Request(book);
                bookStore.getRequests().add(request);
                System.out.println("Запрос на книгу " + bookTitle + " оставлен.");
            } else {
                OrderController order = new OrderController(bookStore);
                order.createOrder(book, OrderStatus.NEW);
            }
        } else
            System.out.println("Книга с названием " + bookTitle + " не найдена в инвентаре.");
    }
    public static void fulfillRequest(BookStore bookStore) {
        if (request.getBook().getStatus() == BookStatus.IN_STOCK) {
            OrderController orderController = new OrderController(bookStore);
            orderController.createOrder(request.getBook(), OrderStatus.NEW);
            bookStore.getRequests().remove(request);
        } else
            System.out.println("Книга " + request.getBook().getTitle() + " недоступна для заказа.");
    }
    public static void fulfillPendingRequests(BookStore bookStore) {
        List<Request> requestsCopy = new ArrayList<>(bookStore.getRequests());
        requestsCopy.forEach(request -> fulfillRequest(bookStore));
    }
}

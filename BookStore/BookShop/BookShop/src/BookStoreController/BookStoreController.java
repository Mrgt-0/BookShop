package BookStoreController;

import java.time.LocalDate;
import java.util.Optional;
import BookStoreModel.*;
import Property.Util;
import Status.*;

public class BookStoreController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    private OrderController orderController;
    public BookStoreController(BookStore bookStore, OrderController orderController){
        this.bookStore=bookStore;
        this.orderController=orderController;
        bookStoreSerializable=new BookStoreSerializable(bookStore);
    }

    public void addBook(Book book) {
        bookStore.getBookInventory().put(book.getTitle(), book);
        RequestController.fulfillPendingRequests(bookStore);
        if(Util.isMarkOrdersAsCompleted())
            fulfillOrder(book.getTitle());
        bookStoreSerializable.saveState();
    }

    public void removeBook(String title) {
        if(bookStore.getBookInventory().containsKey(title)){
            Book book = bookStore.getBookInventory().get(title);
            book.setStatus(BookStatus.OUT_OF_STOCK);
            bookStoreSerializable.saveState();
        }
        else
            System.out.println("Книга с таким названием не найдена");
    }

    public void updateOrderStatus(String bookTitle, OrderStatus status) {
        if(Util.isMarkOrdersAsCompleted()) {
            bookStore.getOrders.stream()
                    .filter(order -> order.getBook().getTitle().equals(bookTitle))
                    .findFirst()
                    .ifPresent(order -> orderController.updateStatus(order, status));
            bookStoreSerializable.saveState();
        }else
            System.out.println("Обновление статуса заказа отключено.");
    }

    public void cancelOrder(String bookTitle) {
        bookStore.getOrders.stream()
                .filter(order -> order.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(orderController::cancelOrder);
        bookStoreSerializable.saveState();
    }

    public void placeOrder(String title) {
        Book book = bookStore.getBookInventory().get(title);
        if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, OrderStatus.NEW);
            bookStore.getOrders.add(order);
            System.out.println("Заказ на книгу: " + book.getTitle());
            bookStoreSerializable.saveState();
        } else if(book!=null){
            System.out.println("Книги: "+ book.getTitle()+"нет на складе. Запрос на эту книгу оставлен");
            RequestController bookRequest=new RequestController(bookStore, book);
            bookRequest.requestBook(bookStore, title);
            bookStoreSerializable.saveState();
        }else
            System.out.println("Книга с таким названием не найдена");
    }

    public void fulfillOrder(String title){
        Optional<Order> orderOptional = bookStore.getOrders.stream()
                .filter(order -> order.getBook().getTitle().equals(title) && order.getStatus() == OrderStatus.NEW)
                .findFirst();
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (Util.isMarkOrdersAsCompleted()) {
                orderController.updateStatus(order, OrderStatus.FULFILLED);
                order.setExecutionDate(LocalDate.now());
                bookStore.setTotalEarnings(order.getBook().getPrice());
                bookStore.setTotalOrdersFulfilled(1);
            } else
                System.out.println("Заказ на книгу " + order.getBook().getTitle() + " ожидает выполнения.");
            bookStoreSerializable.saveState();
        }
    }
}

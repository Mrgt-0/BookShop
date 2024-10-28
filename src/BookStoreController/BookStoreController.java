package BookStoreController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Property.Util;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.*;
@Singleton
public class BookStoreController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    private OrderController orderController;
    @Inject
    private BookRepository bookRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private RequestRepository requestRepository;
    public BookStoreController(BookStore bookStore, OrderController orderController){
        this.bookStore=bookStore;
        this.orderController=orderController;
        bookStoreSerializable=new BookStoreSerializable(bookStore);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
        bookStore.getBookInventory().put(book.getTitle(), book);

        RequestController requestController = new RequestController(bookStore, orderRepository);
        requestController.fulfillPendingRequests(bookStore);

        if (Util.isMarkOrdersAsCompleted())
            fulfillOrder(book.getTitle());

        bookStoreSerializable.saveState();
    }

    public void removeBook(String bookTitle) {
        Book book = bookRepository.getBookByTitle(bookTitle);
        if (book != null) {
            int bookId = book.getBookId();
            bookRepository.delete(bookId);
            bookStore.getBookInventory().remove(bookTitle);
            bookStoreSerializable.saveState();
            System.out.println("Книга " + bookTitle + " удалена.");
        } else {
            System.out.println("Книга с названием " + bookTitle + " не найдена.");
        }
    }

    public void updateOrderStatus(String bookTitle, OrderStatus status) {
        if(Util.isMarkOrdersAsCompleted()) {
            bookStore.getOrders().stream()
                    .filter(order -> order.getBook().getTitle().equals(bookTitle))
                    .findFirst()
                    .ifPresent(order -> {
                        orderController.updateStatus(order, status);
                        orderRepository.updateOrderStatus(order.getOrderId(), status);
                    });
            bookStoreSerializable.saveState();
        }else
            System.out.println("Обновление статуса заказа отключено.");
    }

    public void cancelOrder(String bookTitle) {
        bookStore.getOrders().stream()
                .filter(order -> order.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(order -> {
                    orderController.cancelOrder(order);
                    orderRepository.delete(order.getOrderId());
                });
        bookStoreSerializable.saveState();
    }

    public void placeOrder(String title) {
        Book book = bookStore.getBookInventory().get(title);
        if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, OrderStatus.NEW);
            bookStore.getOrders().add(order);
            orderRepository.save(order);
            System.out.println("Заказ на книгу: " + book.getTitle());
            bookStoreSerializable.saveState();
        } else if (book != null) {
            System.out.println("Книги: " + book.getTitle() + " нет на складе. Запрос на эту книгу оставлен");
            RequestController bookRequest = new RequestController(bookStore, orderRepository);
            bookRequest.requestBook(bookStore, title);
            bookStoreSerializable.saveState();
        } else {
            System.out.println("Книга с таким названием не найдена");
        }
    }

    public void fulfillOrder(String title){
        Optional<Order> orderOptional = bookStore.getOrders().stream()
                .filter(order -> order.getBook().getTitle().equals(title) && order.getStatus() == OrderStatus.NEW)
                .findFirst();

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (Util.isMarkOrdersAsCompleted()) {
                orderController.updateStatus(order, OrderStatus.FULFILLED);
                order.setExecutionDate(LocalDate.now());
                bookStore.setTotalEarnings(order.getBook().getPrice());
                bookStore.setTotalOrdersFulfilled(1);
                orderRepository.update(order);
            } else
                System.out.println("Заказ на книгу " + order.getBook().getTitle() + " ожидает выполнения.");
            bookStoreSerializable.saveState();
        }
    }
}

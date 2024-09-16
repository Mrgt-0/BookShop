package BookStoreController;
import BookStoreModel.*;
import DI.Singleton;
import Status.*;
@Singleton
public class OrderController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    public OrderController(BookStore bookStore){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
    }

    public void createOrder(Book book, OrderStatus status) {
        if (book.getStatus() == BookStatus.IN_STOCK) {
            Order order=new Order(book, status);
            bookStore.getOrders.add(order);
            System.out.println("Заказ на книгу " + order.getBook().getTitle() + " создан.");
            bookStoreSerializable.saveState();
        } else {
            System.out.println("Книга " + book.getTitle() + " недоступна для заказа.");
        }
    }

    public void updateStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        System.out.println("Статус заказа на книгу " + order.getBook().getTitle() + " изменен на " + status + ".");
        bookStoreSerializable.saveState();
    }

    public void cancelOrder(Order order) {
        bookStore.getOrders.remove(order);
        System.out.println("Заказ на книгу " + order.getBook().getTitle() + " отменен.");
        bookStoreSerializable.saveState();
    }
}

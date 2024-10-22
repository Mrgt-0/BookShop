package BookStoreController;
import BookStoreModel.*;
import DI.Inject;
import DI.Singleton;
import Repository.OrderRepository;
import Status.*;
@Singleton
public class OrderController {
    private BookStoreSerializable bookStoreSerializable;
    private BookStore bookStore;
    @Inject
    private OrderRepository orderRepository;

    public OrderController(BookStore bookStore, OrderRepository orderRepository){
        bookStoreSerializable=new BookStoreSerializable(bookStore);
        this.bookStore=bookStore;
        this.orderRepository=orderRepository;
    }

    public void createOrder(Book book, OrderStatus status) {
        if (book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, status);
            bookStore.getOrders().add(order);
            orderRepository.save(order);
            System.out.println("Заказ на книгу " + order.getBook().getTitle() + " создан.");
            bookStoreSerializable.saveState();
        } else {
            System.out.println("Книга " + book.getTitle() + " недоступна для заказа.");
        }
    }

    public void updateStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        orderRepository.update(order);
        System.out.println("Статус заказа на книгу " + order.getBook().getTitle() + " изменен на " + status + ".");
        bookStoreSerializable.saveState();
    }

    public void cancelOrder(Order order) {
        orderRepository.delete(order.getOrderId());
        bookStore.getOrders().remove(order);
        System.out.println("Заказ на книгу " + order.getBook().getTitle() + " отменен.");
        bookStoreSerializable.saveState();
    }
}

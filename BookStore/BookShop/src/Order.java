import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    private Book book;
    private OrderStatus status;
    private LocalDate executionDate;
    private int orderPrice;

    public Order(Book book, OrderStatus status) {
        this.book = book;
        this.status = status;
    }
    public Book getBook() {
        return book;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public LocalDate getExecutionDate(){
        return executionDate;
    }
    public void setExecutionDate(LocalDate executionDate){
        this.executionDate=executionDate;
    }
    public int getOrderPrice(){
        return orderPrice;
    }
    public void createOrder(BookStore bookStore) {
        if (book.getStatus() == BookStatus.IN_STOCK) {
            bookStore.getOrders().add(this);
            System.out.println("Заказ на книгу " + book.getTitle() + " создан.");
        } else {
            System.out.println("Книга " + book.getTitle() + " недоступна для заказа.");
        }
    }
    public void updateStatus(OrderStatus status) {
        this.status = status;
        System.out.println("Статус заказа на книгу " + book.getTitle() + " изменен на " + status + ".");
    }

    public void cancelOrder() {
        System.out.println("Заказ на книгу " + book.getTitle() + " отменен.");
    }

}

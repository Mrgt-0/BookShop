package BookStoreModel;

import java.time.LocalDate;
import Status.*;
public class Order {
    private Book book;
    private OrderStatus status;
    private LocalDate executionDate;
    private int orderPrice;

    public Order(Book book, OrderStatus status){
        this.book=book;
        this.status=status;
    }
    public Book getBook() {
        return book;
    }
    public OrderStatus getStatus(){
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status=status;
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
}

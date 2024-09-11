package BookStoreModel;

import java.io.Serializable;
import java.time.LocalDate;

import Config.ConfigProperty;
import Status.*;
public class Order implements Serializable {
    private int orderId;
    private static int idIncrement=0;
    private Book book;
    private OrderStatus orderStatus;
    private LocalDate executionDate;
    private int orderPrice;

    public Order(Book book, OrderStatus status){
        this.book=book;
        this.orderStatus=status;
        this.orderId=++idIncrement;
    }
    @ConfigProperty
    public int getOrderId;

    @ConfigProperty(type = int.class)
    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    @ConfigProperty
    public Book getBook;

    @ConfigProperty
    public OrderStatus getStatus;

    @ConfigProperty(type = OrderStatus.class)
    public void setStatus(OrderStatus orderStatus){
        this.orderStatus=orderStatus;
    }

    @ConfigProperty
    public LocalDate getExecutionDate(){
        return executionDate;
    }

    @ConfigProperty(type = LocalDate.class)
    public void setExecutionDate(LocalDate executionDate){
        this.executionDate=executionDate;
    }

    @ConfigProperty
    public int getOrderPrice(){
        return orderPrice;
    }

    @ConfigProperty(type = int.class)
    public void setOrderPrice(int orderPrice){
        this.orderPrice=orderPrice;
    }
}

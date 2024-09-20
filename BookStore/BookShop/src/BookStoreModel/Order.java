package BookStoreModel;

import java.io.Serializable;
import java.time.LocalDate;

import Config.ConfigProperty;
import DI.Inject;
import DI.Singleton;
import Status.*;
@Singleton
public class Order implements Serializable {
    private int orderId;
    private static int idIncrement=0;
    @Inject
    private Book book;
    @Inject
    private OrderStatus orderStatus;
    private LocalDate executionDate;
    private int orderPrice;

    public Order(Book book, OrderStatus status){
        this.book=book;
        this.orderStatus=status;
        this.orderId=++idIncrement;
    }

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    public Book getBook(){
        return book;
    }

    public OrderStatus getStatus(){
        return orderStatus;
    }

    public void setStatus(OrderStatus orderStatus){
        this.orderStatus=orderStatus;
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

    public void setOrderPrice(int orderPrice){
        this.orderPrice=orderPrice;
    }
}

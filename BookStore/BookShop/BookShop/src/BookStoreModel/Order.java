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

    @ConfigProperty
    public int getOrderId(){
        return orderId;
    }

    @ConfigProperty(type = int.class)
    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    @ConfigProperty
    public Book getBook(){
        return book;
    }

    @ConfigProperty
    public OrderStatus getStatus(){
        return orderStatus;
    }

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

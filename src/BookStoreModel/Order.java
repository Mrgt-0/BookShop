package BookStoreModel;

import java.io.Serializable;
import java.time.LocalDate;

import Status.*;

public class Order implements Serializable {
    private int orderId;
    private static int idIncrement=0;
    private Book book;
    private OrderStatus orderStatus;
    private LocalDate executionDate;
    private Double orderPrice;

    public Order(Book book, OrderStatus status){
        this.book=book;
        this.orderStatus=status;
        this.orderId=++idIncrement;
    }

    public Order(OrderStatus orderStatus, LocalDate executionDate, Double orderPrice){
        this.orderStatus=orderStatus;
        this.executionDate=executionDate;
        this.orderPrice=orderPrice;
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

    public Double getOrderPrice(){
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice){
        this.orderPrice=orderPrice;
    }
}

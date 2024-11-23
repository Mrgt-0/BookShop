package DTO;

import Model.Book;
import Status.OrderStatus;

import java.time.LocalDate;

public class OrderDTO {
    private int orderId;
    private Book book;
    private OrderStatus orderStatus;
    private LocalDate executionDate;
    private Double orderPrice;
    public OrderDTO(int orderId, OrderStatus orderStatus, LocalDate executionDate, Double orderPrice){
        this.orderId=orderId;
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

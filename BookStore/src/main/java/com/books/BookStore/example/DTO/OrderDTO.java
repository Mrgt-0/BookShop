package com.books.BookStore.example.DTO;

import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Service.OrderService;
import com.books.BookStore.example.Status.OrderStatus;

import java.time.LocalDate;

public class OrderDTO {
    private Book book;
    private OrderStatus orderStatus;
    private LocalDate executionDate;
    private Double orderPrice;
    public OrderDTO(Book book, OrderStatus orderStatus, LocalDate executionDate, Double orderPrice){
        this.book=book;
        this.orderStatus=orderStatus;
        this.executionDate=executionDate;
        this.orderPrice=orderPrice;
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

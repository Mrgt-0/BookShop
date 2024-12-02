package com.books.BookStore.example.DTO;

import com.books.BookStore.example.Status.BookStatus;

import java.time.LocalDate;

public class BookDTO {
    private int bookId;
    private String title;
    private String author;
    private BookStatus status;
    private LocalDate publishDate;
    private double price;
    private String description;

    public BookDTO(int bookId, String title, String author, BookStatus status, LocalDate publishDate, double price, String description) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.status = status;
        this.publishDate = publishDate;
        this.price = price;
        this.description = description;
    }

    public int getBookId(){
        return bookId;
    }

    public void setBookId(int bookId){
        this.bookId=bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() { return author; }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDate getPublishDate(){
        return publishDate;
    }

    public double getPrice(){
        return price;
    }

    public String getDescription(){
        return description;
    }
}

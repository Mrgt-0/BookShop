package BookStoreModel;

import java.io.Serializable;
import java.time.LocalDate;

import Config.ConfigProperty;
import DI.Singleton;
import Status.*;

public class Book implements Serializable{
    private int bookId;
    private static int idIncrement=0;
    private String title;
    private String author;
    private BookStatus status;
    private LocalDate publishDate;
    private double price;
    private String description;

    public Book(String title, String author, BookStatus status, LocalDate publishDate, double price, String desctiption) {
        this.title = title;
        this.author=author;
        this.status = status;
        this.publishDate=publishDate;
        this.price=price;
        this.description=desctiption;
        this.bookId=++idIncrement;
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

    @Override
    public String toString(){
        return "Книга{" +
                "Название='" + title + '\'' +
                ", статус=" + status +
                ", дата публикации=" + publishDate +
                ", цена=" + price +
                '}';
    }
}

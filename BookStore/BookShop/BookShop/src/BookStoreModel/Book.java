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
    private BookStatus status;
    private LocalDate publishDate;
    private double price;
    private String description;

    public Book(String title, BookStatus status, LocalDate publishDate, double price, String desctiption) {
        this.title = title;
        this.status = status;
        this.publishDate=publishDate;
        this.price=price;
        this.description=desctiption;
        this.bookId=++idIncrement;
    }

    @ConfigProperty
    public int getBookId(){
        return bookId;
    }

    @ConfigProperty(type = int.class)
    public void setBookId(int bookId){
        this.bookId=bookId;
    }

    @ConfigProperty
    public String getTitle() {
        return title;
    }

    @ConfigProperty
    public BookStatus getStatus() {
        return status;
    }

    @ConfigProperty(type = BookStatus.class)
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @ConfigProperty
    public LocalDate getPublishDate(){
        return publishDate;
    }

    @ConfigProperty
    public double getPrice(){
        return price;
    }

    @ConfigProperty
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

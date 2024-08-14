package BookStoreModel;

import java.time.LocalDate;
import Status.*;

public class Book {
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
    }
    public String getTitle() {
        return title;
    }
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

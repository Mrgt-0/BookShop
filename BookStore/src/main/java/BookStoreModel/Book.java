package BookStoreModel;

import Status.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Book")
public class Book implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int bookId;
    private static int idIncrement=0;
    private String title;
    private String author;

   @Enumerated(EnumType.STRING)
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

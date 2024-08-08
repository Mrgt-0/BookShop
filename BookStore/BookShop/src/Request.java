import java.util.ArrayList;
import java.util.List;

public class Request {
    private Book book;
    private int requestCount=1;

    public Request(Book book) {
        this.book = book;
    }
    public Book getBook() {
        return book;
    }
    public int getRequestCount(){
        return requestCount;
    }
    public void incrementRequestCount(){
        this.requestCount++;
    }
    public void requestBook(BookStore bookStore, String bookTitle) {
        Book book = bookStore.getBookInventory().get(bookTitle);
        if (book != null) {
            if (book.getStatus() == BookStatus.OUT_OF_STOCK) {
                Request request = new Request(book);
                bookStore.getRequests().add(request);
                System.out.println("Запрос на книгу " + bookTitle + " оставлен.");
            } else {
                Order order = new Order(book, OrderStatus.NEW);
                order.createOrder(bookStore);
            }
        } else
            System.out.println("Книга с названием " + bookTitle + " не найдена в инвентаре.");
    }
    public void fulfillRequest(BookStore bookStore) {
        if (book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, OrderStatus.NEW);
            order.createOrder(bookStore);
            bookStore.getRequests().remove(this);
        } else
            System.out.println("Книга " + book.getTitle() + " недоступна для заказа.");
    }
    public static void fulfillPendingRequests(BookStore bookStore) {
        List<Request> requestsCopy = new ArrayList<>(bookStore.getRequests());
        requestsCopy.forEach(request -> request.fulfillRequest(bookStore));
    }
}

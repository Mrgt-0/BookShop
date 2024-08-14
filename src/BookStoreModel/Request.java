package BookStoreModel;

public class Request{
    private Book book;
    private int requestCount=1;

    public Request(Book book) {
        this.book=book;
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

}

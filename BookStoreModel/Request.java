package BookStoreModel;

public class Request{
    private int requestId;
    private static int id=0;
    private Book book;
    private int requestCount=1;

    public Request(Book book) {
        this.book=book;
        this.requestId=++id;
    }
    public int getRequestId(){
        return requestId;
    }
    public void setRequestId(int requestId){
        this.requestId=requestId;
    }
    public Book getBook() {
        return book;
    }
    public int getRequestCount(){
        return requestCount;
    }
    public void setRequestCount(int requestCount){
        this.requestCount=requestCount;
    }
    public void incrementRequestCount(){
        this.requestCount++;
    }

}

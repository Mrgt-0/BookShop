package BookStoreModel;

import java.io.Serializable;

public class Request implements Serializable {
    private int requestId;
    private static int idIncrement=0;
    private Book book;
    private int requestCount=1;

    public Request(Book book) {
        this.book=book;
        this.requestId=++idIncrement;
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

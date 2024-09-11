package BookStoreModel;

import Config.ConfigProperty;

import java.io.Serializable;
import java.lang.reflect.Type;

public class Request implements Serializable {
    private int requestId;
    private static int idIncrement=0;
    private Book book;
    private int requestCount=1;

    public Request(Book book) {
        this.book=book;
        this.requestId=++idIncrement;
    }

    @ConfigProperty
    public int getRequestId;

    @ConfigProperty(type = int.class)
    public void setRequestId(int requestId){
        this.requestId=requestId;
    }

    @ConfigProperty
    public Book getBook;

    @ConfigProperty
    public int getRequestCount(){
        return requestCount;
    };

    @ConfigProperty(type = int.class)
    public void setRequestCount(int requestCount){
        this.requestCount=requestCount;
    }

    @ConfigProperty
    public void incrementRequestCount(){
        this.requestCount++;
    }
}

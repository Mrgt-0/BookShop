package BookStoreModel;

import Config.ConfigProperty;
import DI.Inject;
import DI.Singleton;

import java.io.Serializable;
import java.lang.reflect.Type;

@Singleton
public class Request implements Serializable {
    private int requestId;
    private static int idIncrement=0;
    @Inject
    private Book book;
    private int requestCount=1;

    public Request(Book book) {
        this.book=book;
        this.requestId=++idIncrement;
    }

    @ConfigProperty
    public int getRequestId(){
        return requestId;
    }

    @ConfigProperty(type = int.class)
    public void setRequestId(int requestId){
        this.requestId=requestId;
    }

    @ConfigProperty
    public Book getBook(){
        return book;
    }

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

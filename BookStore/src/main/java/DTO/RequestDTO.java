package DTO;

import Model.Book;

public class RequestDTO {
    private int requestId;
    private Book book;
    public RequestDTO(int requestId, Book book){
        this.requestId=requestId;
        this.book=book;
    }
    public int getRequestId(){
        return requestId;
    }

    public void setRequestId(int requestId){
        this.requestId=requestId;
    }

    public Book getBook(){
        return book;
    }
}

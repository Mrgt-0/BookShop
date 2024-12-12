package com.books.BookStore.example.Model;

import com.books.BookStore.example.Service.BookStoreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class BookStoreSerializable {
    private static final String BOOKSTORE_STATE_FILE = "bookstore_state.ser";
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);
    private final BookStore bookStore;
    public BookStoreSerializable(BookStore bookStore){
        bookStore=loadState();
            this.bookStore=bookStore;
    }
    public void saveState(){
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(BOOKSTORE_STATE_FILE))){
            out.writeObject(bookStore);
            logger.info("Состояние программы сохранено.");
        }catch (IOException e){
            logger.error("Error: "+e.getMessage());
        }
    }
    private BookStore loadState(){
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(BOOKSTORE_STATE_FILE))){
            BookStore bookStore=(BookStore)in.readObject();
            logger.info("Состояние программы восстановлено.");
            return bookStore;
        }catch (IOException  | ClassNotFoundException e) {
            logger.error("Error: " + e.getMessage());
            return null;
        }
    }
}

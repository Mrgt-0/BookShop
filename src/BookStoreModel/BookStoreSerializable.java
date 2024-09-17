package BookStoreModel;

import java.io.*;

public class BookStoreSerializable {
    private static final String BOOKSTORE_STATE_FILE = "bookstore_state.ser";
    private final BookStore bookStore;
    public BookStoreSerializable(BookStore bookStore){
        bookStore=loadState();
            this.bookStore=bookStore;
    }
    public void saveState(){
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(BOOKSTORE_STATE_FILE))){
            out.writeObject(bookStore);
            System.out.println("Состояние программы сохранено.");
        }catch (IOException e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    private BookStore loadState(){
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(BOOKSTORE_STATE_FILE))){
            BookStore bookStore=(BookStore)in.readObject();
            System.out.println("Состояние программы восстановлено.");
            return bookStore;
        }catch (IOException  | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

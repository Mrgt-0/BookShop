package BookStoreController;

import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.Order;
import BookStoreModel.Request;
import DI.DependencyInjector;
import Status.BookStatus;
import Status.OrderStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Importer {
    public Map<String, Book> booksImporter() {
        Map<String, Book> importedBooks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(readFile()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 7) {
                    String title = bookData[1];
                    String author = bookData[2];
                    BookStatus status = BookStatus.valueOf(bookData[3].toUpperCase());
                    LocalDate publishDate = LocalDate.parse(bookData[4]);
                    double price = Double.parseDouble(bookData[5]);
                    String description = bookData[6];
                    Book book = new Book(title, author, status, publishDate, price, description);
                    book.setBookId(Integer.parseInt(bookData[0]));
                    importedBooks.put(book.getTitle(), book);
                } else
                    System.out.println("Некорректный формат данных в файле: " + line);
            }
            System.out.println("Импорт книг завершен успешно.");
            return importedBooks;
        } catch (IOException e) {
            System.out.println("Ошибка при импорте книг: " + e.getMessage());
            return null;
        }
    }

    private String readFile(){
        System.out.print("Введите название файла для импорта: ");
        return System.console().readLine();
    }

    public  List<Order> ordersImporter() {
        BookStore bookStore = DependencyInjector.getInstance(BookStore.class);
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(readFile()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(",");
                if (orderData.length == 4) {
                    int id = Integer.parseInt(orderData[0]);
                    String bookTitle = orderData[1];
                    OrderStatus status = OrderStatus.valueOf(orderData[2]);
                    LocalDate executionDate = LocalDate.parse(orderData[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    int orderPrice = Integer.parseInt(orderData[4]);

                    List<Book> books = bookStore.getBookInventory((a, b) -> a.getTitle().compareTo(b.getTitle()));
                    Book book = books.stream().filter(b -> b.getTitle().equals(bookTitle)).findFirst().orElse(null);
                    if (book != null) {
                        Order order = new Order(book, status);
                        order.setOrderId(id);
                        order.setExecutionDate(executionDate);
                        order.setOrderPrice(orderPrice);
                        orders.add(order);
                    } else
                        System.out.println("Book not found in inventory: " + bookTitle);
                } else
                    System.out.println("Некорректный формат данных в файле: " + line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при импорте книг: " + e.getMessage());
            return null;
        }
        return orders;
    }

    public List<Request> requestsImporter(){
        BookStore bookStore=DependencyInjector.getInstance(BookStore.class);
        List<Request> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(readFile()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] requestData = line.split(",");
                if (requestData.length == 3) {
                    int id = Integer.parseInt(requestData[0]);
                    String bookTitle = requestData[1];
                    int requestCount= Integer.parseInt(requestData[2]);
                    Book book = bookStore.getBookInventory().get(bookTitle);
                    if (book != null) {
                        Request request=new Request(book);
                        request.setRequestId(id);
                        request.setRequestCount(requestCount);
                        requests.add(request);
                    } else
                        System.out.println("Book not found in inventory: " + bookTitle);
                }else
                    System.out.println("Некорректный формат данных в файле: " + line);
            }
            }catch (IOException e) {
            System.out.println("Ошибка при импорте книг: " + e.getMessage());
            return null;
        }
        return requests;
    }
}

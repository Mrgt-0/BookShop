package BookStoreController;

import BookStoreModel.Book;
import BookStoreModel.Order;
import BookStoreModel.Request;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Exporter {
    public void booksExport(Map<String, Book> books){
        if (!readFile().isEmpty()) {
            try (FileWriter writer = new FileWriter(readFile())) {
                writer.write("Id, Title, Status, Publish date, Price, Description\n");
                for (Book book : books.values()) {
                    writer.write(book.getBookId() + "," +
                            book.getTitle() + "," +
                            book.getStatus() + "," +
                            book.getPublishDate() + "," +
                            book.getPrice() + "," +
                            book.getDescription() + "\n");
                }
                System.out.println("Экспорт книг в файл успешно завершен.");
            } catch (IOException e) {
                System.out.println("Ошибка при экспорте книг: " + e.getMessage());
            }
        } else
            System.out.println("Некорректный выбор");
    }
    private String readFile(){
        System.out.print("Введите название файла для экспорта: ");
        return System.console().readLine();
    }
    public void ordersExport(List<Order> orders){
        if (!readFile().isEmpty()) {
            try (FileWriter writer = new FileWriter(readFile())) {
                writer.write("Id, Book, Status, Execution date, Price");
                for (Order order : orders) {
                    writer.write(order.getOrderId() + "," +
                            order.getBook() + "," +
                            order.getStatus() + "," +
                            order.getExecutionDate() + "," +
                            order.getOrderPrice() + "\n");
                }
                System.out.println("Экспорт заказов в файл успешно завершен.");
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }else
            System.out.println("Некорректный выбор");
    }
    public void requestsExport(List<Request> requests){
        if (!readFile().isEmpty()) {
            try (FileWriter writer = new FileWriter(readFile())) {
                writer.write("Id, Book, request count");
                for (Request request : requests) {
                    writer.write(request.getRequestId() + "," +
                            request.getBook().getTitle() + "," +
                            request.getRequestCount() + "\n");
                }
                System.out.println("Экспорт заказов в файл успешно завершен.");
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }else
            System.out.println("Некорректный выбор");
    }
}

package BookStoreService;

import BookStoreModel.Book;
import BookStoreModel.Order;
import BookStoreModel.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping
public class Exporter {
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    @GetMapping("/export")
    public String booksExport(Map<String, Book> books){
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
                return "redirect:/books";
            } catch (IOException e) {
                logger.error("Ошибка при экспорте книг: " + e.getMessage());
            }
        } else
            logger.error("некорректный выбор");
        return "error";
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

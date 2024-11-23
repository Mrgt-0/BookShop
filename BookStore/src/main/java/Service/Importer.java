package Service;

import Model.Book;
import Model.BookStore;
import Model.Order;
import Model.Request;
import Status.BookStatus;
import Status.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping
public class Importer {
    @Autowired
    private BookStore bookStore;
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    @GetMapping("/importBooks")
    public String booksImporter(@RequestParam String fileName, Model model) {
        Map<String, Book> importedBooks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
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
                    logger.error("Некорректный формат данных в файле: " + line);
            }
            logger.error("Импорт книг завершен успешно.");
            model.addAttribute("message", "Импорт завершен успешно. Импортировано книг: " + importedBooks.size());
            return "redirect:/books";
        } catch (IOException e) {
            logger.error("Ошибка при импорте книг: " + e.getMessage());
            model.addAttribute("message", "Ошибка при импорте: " + e.getMessage());
            return "importBooks";
        }
    }

    @GetMapping("/importOrders")
    public  String ordersImporter(@RequestParam String fileName, Model model) {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
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
                        order.setOrderPrice((double) orderPrice);
                        orders.add(order);
                    } else
                        logger.error("Book not found in inventory: " + bookTitle);
                } else
                    logger.error("Некорректный формат данных в файле: " + line);
            }
        } catch (IOException e) {
            logger.error("Ошибка при импорте книг: " + e.getMessage());
            return "importOrders";
        }
        return "redirect:/books";
    }

    @GetMapping("/importRequests")
    public String requestsImporter(@RequestParam String fileName, Model model){
        List<Request> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
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
                        logger.error("Book not found in inventory: " + bookTitle);
                }else
                    logger.error("Некорректный формат данных в файле: " + line);
            }
            }catch (IOException e) {
            logger.error("Ошибка при импорте книг: " + e.getMessage());
            return null;
        }
        return "redirect:/books";
    }
}

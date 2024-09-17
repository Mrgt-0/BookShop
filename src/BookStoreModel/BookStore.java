package BookStoreModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import Config.ConfigProperty;
import DI.Inject;
import DI.Singleton;
import Status.*;

@Singleton
public class BookStore implements Serializable{
    @Inject
    Map<String, Book> bookInventory;
    @Inject
    private List<Order> orders;
    @Inject
    private List<Request> requests;
    private double totalEarnings=0.0;
    private int totalOrdersFulfilled=0;

    public BookStore(Map<String, Book> bookInventory, List<Order> orders, List<Request> requests) {
        this.bookInventory = bookInventory;
        this.orders = orders;
        this.requests = requests;
    }

    @ConfigProperty
    public Map<String, Book> getBookInventory() {
        if (this.bookInventory == null) {
            this.bookInventory = new HashMap<>();
        }
        return this.bookInventory;
    }

    @ConfigProperty
    public List<Request> getRequests() {
        if (this.requests == null) {
            this.requests = new ArrayList<>();
        }
        return this.requests;
    }

    @ConfigProperty
    public List<Order> getOrders;

    @ConfigProperty(type = double.class)
    public void setTotalEarnings(double totalEarnings){
        this.totalEarnings=totalEarnings;
    }

    @ConfigProperty(type = int.class)
    public void setTotalOrdersFulfilled(int totalOrdersFulfilled){
        this.totalOrdersFulfilled=totalOrdersFulfilled;
    }

    public List<Order> getFullfilledOrders(LocalDate startDate, LocalDate endDate, Comparator<Order> comparator) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.FULFILLED)
                .filter(order -> order.getExecutionDate()!=null&& order.getExecutionDate().isAfter(startDate)
                        && order.getExecutionDate().isBefore(endDate))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Book> getOldBooks(LocalDate thresholdDate, Comparator<Book> comparator) {
        return bookInventory.values().stream()
                .filter(book -> book.getStatus() == BookStatus.IN_STOCK
                        && book.getPublishDate().isBefore(thresholdDate))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Book> getBookInventory(Comparator<Book> comparator) {
        return bookInventory.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Order> getOrders(Comparator<Order> comparator) {
        return orders.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Request> getRequests(Comparator<Request> comparator) {
        return requests.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

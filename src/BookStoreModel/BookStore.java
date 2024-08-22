package BookStoreModel;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import Status.*;

public class BookStore {
    Map<String, Book> bookInventory;
    private List<Order> orders;
    private List<Request> requests;
    private double totalEarnings=0.0;
    private int totalOrdersFulfilled=0;

    public BookStore() {
        this.bookInventory = new HashMap<>();
        this.orders = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    public Map<String, Book> getBookInventory() {
        return bookInventory;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setTotalEarnings(double value) {
        this.totalEarnings+=value;
    }

    public void setTotalOrdersFulfilled(int value) {
        this.totalOrdersFulfilled+=value;
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

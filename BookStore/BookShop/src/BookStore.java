import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookStore {
    private Map<String, Book> bookInventory;
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
    public double getTotalEarnings() {
        return totalEarnings;
    }

    public int getTotalOrdersFulfilled() {
        return totalOrdersFulfilled;
    }
    public void addBook(Book book) {
        bookInventory.put(book.getTitle(), book);
        Request.fulfillPendingRequests(this);
    }
    public void removeBook(String title) {
        Book book = bookInventory.get(title);
        book.setStatus(BookStatus.OUT_OF_STOCK);
    }
    public void updateOrderStatus(String bookTitle, OrderStatus status) {
        orders.stream()
                .filter(order -> order.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(order -> order.updateStatus(status));
    }
    public void cancelOrder(String bookTitle) {
        orders.stream()
                .filter(order -> order.getBook().getTitle().equals(bookTitle))
                .findFirst()
                .ifPresent(Order::cancelOrder);
    }
    public void placeOrder(String title) {
        Book book = bookInventory.get(title);
        if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
            Order order = new Order(book, OrderStatus.NEW);
            orders.add(order);
            System.out.println("Заказ на книгу: " + book.getTitle());
        } else {
            Request boookRequest=new Request(book);
            boookRequest.requestBook(this, title);
        }
    }
    public void fulfillOrder(String title){
        Optional<Order> orderOptional = orders.stream()
                .filter(order -> order.getBook().getTitle().equals(title) && order.getStatus() == OrderStatus.NEW)
                .findFirst();
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.updateStatus(OrderStatus.FULFILLED);
            order.setExecutionDate(LocalDate.now());
            totalEarnings += order.getBook().getPrice();
            totalOrdersFulfilled++;
        }
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

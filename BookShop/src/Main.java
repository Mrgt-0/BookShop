import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class Main {
    private static BookStore bookStore = new BookStore();
    public static void main(String[] args) {
        ArrayList<Book> books=new ArrayList<>();
        Book warAndPeace=new Book("Война и мир", BookStatus.IN_STOCK, LocalDate.of(1873, 1, 10), 500,
                "роман-эпопея Льва Толстого, посвящённый жизни российского общества во времена правления Александра I.");
        Book crimeAndPunishment=new Book("Преступление и наказание", BookStatus.IN_STOCK, LocalDate.of(1866, 7, 22), 450,
                "В романе рассказывается об отчуждении студента Раскольникова, который решает совершить идеальное преступление, " +
                        "чтобы философски доказать своё превосходство над другими. В романе прослеживаются глубины его душевного распада, когда он приходит к пониманию психологических последствий того, что он убийца.");
        Book annaKarenina=new Book("Анна Каренина", BookStatus.OUT_OF_STOCK, LocalDate.of(1875, 11, 9), 390,
                "это трагическая история любви замужней женщины и офицера Алексея Вронского на фоне счастливого брака " +
                        "Константина Левина и Кити Щербацкой. Помимо размышлений об отношениях героев, Толстой в романе показывает масштабную панораму жизни и нравов России второй половины XIX века и делает множество " +
                        "психологических и философских отступлений, превращая семейную драму в монументальный роман о вечных темах и проблемах.");
        bookStore.addBook(warAndPeace);
        bookStore.addBook(crimeAndPunishment);
        bookStore.addBook(annaKarenina);

        bookStore.placeOrder("Преступление и наказание");
        bookStore.fulfillOrder("Преступление и наказание");

        bookStore.placeOrder("Война и мир");

        bookStore.cancelOrder("Война и мир");

        Book book4 = new Book("Скотный двор", BookStatus.IN_STOCK, LocalDate.of(1945, 8, 17), 5.99, "");
        bookStore.addBook(book4);

        bookStore.placeOrder("Анна Каренина");

        System.out.println("Книги (отсортированы по названию):");
        bookStore.getBookInventory(Comparator.comparing(Book::getTitle)).forEach(System.out::println);
        System.out.println();

        System.out.println("Заказы (отсортированы по дате исполнения):");
        bookStore.getOrders(Comparator.comparing(order -> order.getExecutionDate() != null ? order.getExecutionDate() : LocalDate.MAX))
                .forEach(order -> {
                    System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Статус: " + order.getStatus());
                });

        System.out.println("Запросы (отсортированы по колличеству запросов):");
        bookStore.getRequests(Comparator.comparingInt(Request::getRequestCount).reversed()).forEach(request -> {
            System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
        });
        System.out.println();

        LocalDate startDate = LocalDate.of(1900, 1, 1);
        LocalDate endDate = LocalDate.of(2100, 12, 31);
        System.out.println("Выполненные заказы (отсортированы по дате):");
        bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getExecutionDate)).forEach(order -> {
            System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
        });
        System.out.println();

        System.out.println("Общий доход: " + bookStore.getTotalEarnings());
        System.out.println("Общее колличество выполненных заказов: " + bookStore.getTotalOrdersFulfilled());
        System.out.println();

        LocalDate thresholdDate = LocalDate.now().minusMonths(6);
        System.out.println("Старые книги (отсортированы по дате публикации):");
        bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPublishDate)).forEach(System.out::println);
    }
}
import BookStoreModel.Book;
import BookStoreModel.BookStore;
import ConsoleUI.Builder;
import BookStoreController.BookStoreController;
import BookStoreController.OrderController;
import ConsoleUI.BuilderController;
import ConsoleUI.Navigator;
import Status.BookStatus;

import java.time.LocalDate;
import java.util.ArrayList;

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

        OrderController orderController=new OrderController(bookStore);
        BookStoreController bookStoreController=new BookStoreController(bookStore, orderController);
        bookStoreController.addBook(warAndPeace);
        bookStoreController.addBook(crimeAndPunishment);
        bookStoreController.addBook(annaKarenina);

        Navigator navigator=new Navigator();
        Builder builder=new Builder(bookStore, navigator);
        BuilderController builderController = new BuilderController(builder, navigator);
        builderController.run();
    }
}
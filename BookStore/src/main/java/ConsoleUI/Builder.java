package ConsoleUI;

import BookStoreController.BookStoreController;
import BookStoreController.Exporter;
import BookStoreController.Importer;
import BookStoreController.OrderController;
import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.Order;
import BookStoreModel.Request;
import DI.Inject;
import DI.Singleton;
import Property.Util;
import Repository.BookRepository;
import Repository.OrderRepository;
import Repository.RequestRepository;
import Status.BookStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Singleton
public class Builder {
    @Inject
    private BookStore bookStore;
    @Inject
    private BookStoreController bookStoreController;
    @Inject
    private OrderController orderController;

    @Inject
    private BookRepository bookRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private RequestRepository requestRepository;
    @Inject
    private Navigator navigator;
    @Inject
    private ActionHelper action;
    @Inject
    private Importer importer;
    @Inject
    private Exporter exporter;

    public Builder(BookStore bookStore){
        this.bookStore=bookStore;
        this.navigator=new Navigator();
        orderController=new OrderController(bookStore, orderRepository);
        bookStoreController=new BookStoreController(bookStore, orderController);
        exporter=new Exporter();
        importer=new Importer();
        action=new ActionHelper();
    }

    public Menu buildUserMenu(){
        MenuItem exportBookMenuItem=new MenuItem("Экспорт книг", ()->{
            Menu exportBookMenu=buildExportBooksMenu(buildAdminMenu(navigator));
            navigator.navigate(exportBookMenu);
        });
        List<MenuItem> items=new ArrayList<>();
        items.add(exportBookMenuItem);
        items.add(new MenuItem("Оформить заказ", ()-> {
            String title=action.readBookTitle();
            if(bookStore.getBookInventory().get(title)!=null){
                bookStoreController.placeOrder(title);
            }
            else
                System.out.println("Книга с таким названием не найдена");
        }));
        items.add(new MenuItem("Посмотреть описание книги", ()->{
            Book book=bookStore.getBookInventory().get(action.readBookTitle());
            if(book!=null)
                System.out.println(book.getDescription());
            else
                System.out.println("Книга с таким названием не найдена");
        }));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));
        return new Menu("Меню пользователя", (ArrayList<MenuItem>) items);
    }

    public Menu buildAdminMenu(Navigator navigator){
        MenuItem addBookMenuItem=new MenuItem("Добавить книгу", ()-> {
            Menu addBookMenu = buildAddBookMenu();
            navigator.navigate(addBookMenu);
        });
        MenuItem removeBookMenuItem=new MenuItem("Удалить книгу", ()->{
            Menu removeBookMenu=buildRemoveBookMenu();
            navigator.navigate(removeBookMenu);
        });
        MenuItem exportBookMenuItem=new MenuItem("Экспорт книг", ()->{
            Menu exportBookMenu=buildExportBooksMenu(buildAdminMenu(navigator));
            navigator.navigate(exportBookMenu);
        });
        MenuItem exportOrderMenuItem=new MenuItem("Экспорт заказов", ()->{
            Menu exportOrderMenu=buildExportOrdersMenu(buildAdminMenu(navigator));
            navigator.navigate(exportOrderMenu);
        });
        MenuItem exportRequestMenuItem=new MenuItem("Экспорт запросов", ()->{
            Menu exportRequestMenu=buildExportRequestsMenu(buildAdminMenu(navigator));
            navigator.navigate(exportRequestMenu);
        });
        List<MenuItem> items=new ArrayList<>();
        items.add(exportBookMenuItem);
        items.add(new MenuItem("Импорт книг", ()->importer.booksImporter()));
        items.add(addBookMenuItem);
        items.add(removeBookMenuItem);
        items.add(exportOrderMenuItem);
        items.add(new MenuItem("Импорт заказов", ()->importer.ordersImporter()));
        items.add(exportRequestMenuItem);
        items.add(new MenuItem("Импорт запросов", ()->importer.requestsImporter()));
        items.add(new MenuItem("Посмотреть все выполненные заказы за период", ()->displayeExecuteOrders(bookStore.getOrders())));
        items.add(new MenuItem("Посмотреть не проданный книги", ()->displayOldBooksMenu(bookStore.getBookInventory())));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));

        return new Menu("Меню администратора", (ArrayList<MenuItem>) items);
    }

    private Menu buildExportBooksMenu(Menu nextMenu){
        List<MenuItem> items=new ArrayList<>();
        items.add(new MenuItem("Экспорт книг в консоль", ()->displayBookMenu(bookStore.getBookInventory(), nextMenu)));
        items.add(new MenuItem("Экспорт книг в файл", ()->exporter.booksExport(bookStore.getBookInventory())));
        items.add(new MenuItem("Назад", ()->navigator.navigate(nextMenu)));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));

        return new Menu("Меню экспорта книг", (ArrayList<MenuItem>) items);
    }

    private Menu buildExportOrdersMenu(Menu nextMenu){
        List<MenuItem> items=new ArrayList<>();
        items.add(new MenuItem("Экспорт заказов в консоль", ()->displayOrderMenu(bookStore.getOrders())));
        items.add(new MenuItem("Экспорт заказов в файл", ()->exporter.ordersExport(bookStore.getOrders())));
        items.add(new MenuItem("Назад", ()->navigator.navigate(nextMenu)));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));

        return new Menu("Меню экспорта заказов", (ArrayList<MenuItem>) items);
    }

    private Menu buildExportRequestsMenu(Menu nextMenu){
        List<MenuItem> items=new ArrayList<>();
        items.add(new MenuItem("Экспорт запросов в консоль", ()->displayRequestsMenu(bookStore.getRequests())));
        items.add(new MenuItem("Экспорт запросов в файл", ()->exporter.requestsExport(bookStore.getRequests())));
        items.add(new MenuItem("Назад", ()->navigator.navigate(nextMenu)));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));

        return new Menu("Меню экспорта запросов", (ArrayList<MenuItem>) items);
    }

    private Menu buildAddBookMenu(){
        List<MenuItem> items=new ArrayList<>();
        items.add(new MenuItem("Данные книги", ()->bookStoreController.addBook(new Book(action.readBookTitle(), action.readBookAuthor(), BookStatus.IN_STOCK,
                action.readDate(), action.readPrice(), action.readDescription()))));
        items.add(new MenuItem("Назад", ()->{}));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));

        return new Menu("Добавление книги", (ArrayList<MenuItem>) items);
    }

    private Menu buildRemoveBookMenu(){
        List<MenuItem> items=new ArrayList<>();
        items.add(new MenuItem("Название", ()->bookStoreController.removeBook(action.readBookTitle())));
        items.add(new MenuItem("Назад", ()->{}));
        items.add(new MenuItem("Выйти", ()->System.exit(0)));

        return new Menu("Удаление книги", (ArrayList<MenuItem>) items);
    }

    private void displayBookMenu(Map<String, Book> books, Menu nextMenu){
        System.out.println("Сортировака книг: ");
        List<MenuItem> sortItems=new ArrayList<>();
        sortItems.add(new MenuItem("по названию", ()-> {
            bookStore.getBookInventory(Comparator.comparing(Book::getTitle))
                    .forEach(System.out::println);
            navigator.navigate(nextMenu);
        }));
        sortItems.add(new MenuItem("по дате публикации: ", ()->{
            bookStore.getBookInventory(Comparator.comparing(Book::getPublishDate))
                    .forEach(System.out::println);
            navigator.navigate(nextMenu);
        }));
        sortItems.add(new MenuItem("по цене: ", ()->{
            bookStore.getBookInventory(Comparator.comparing(Book::getPrice))
                    .forEach(System.out::println);
            navigator.navigate(nextMenu);
        }));
        sortItems.add(new MenuItem("по наличию на складе", ()->{
            bookStore.getBookInventory(Comparator.comparing(Book::getStatus))
                    .forEach(System.out::println);
            navigator.navigate(nextMenu);
        }));
        sortItems.add(new MenuItem("назад", ()->navigator.navigate(nextMenu)));
        sortItems.add(new MenuItem("выход", ()->System.exit(0)));
        Menu sortMenu=new Menu("Меню сортировки", (ArrayList<MenuItem>) sortItems);
        navigator.navigate(sortMenu);
    }

    private void displayOrderMenu(List<Order> orders){
        System.out.println("Сортировка заказов: ");
        List<MenuItem> sortItems=new ArrayList<>();
        sortItems.add(new MenuItem("дате исполнения: ", ()->{
            bookStore.getOrders(Comparator.comparing(order -> order.getExecutionDate() != null ? order.getExecutionDate() : LocalDate.MAX))
                    .forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Статус: " + order.getStatus());
                    });
        }));
        sortItems.add(new MenuItem("по цене: ", ()->{
            bookStore.getOrders(Comparator.comparing(order -> order.getOrderPrice()))
                    .forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Цена заказа: " + order.getOrderPrice());
                    });
        }));
        sortItems.add(new MenuItem("по статусу: ", ()->{
            bookStore.getOrders(Comparator.comparing(order -> order.getStatus()))
                    .forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Статус заказа" + order.getStatus());
                    });
        }));
        sortItems.add(new MenuItem("назад", ()->{}));
        sortItems.add(new MenuItem("выход", ()->System.exit(0)));
        Menu sortMenu=new Menu("Меню сортировки", (ArrayList<MenuItem>) sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }

    private void displayRequestsMenu(List<Request> requests){
        System.out.println("Сортировка запросов: ");
        List<MenuItem> sortItems=new ArrayList<>();
        sortItems.add(new MenuItem("по количеству запросов: ", ()->{
            bookStore.getRequests(Comparator.comparingInt(Request::getRequestCount).reversed()).forEach(request -> {
                System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
            });
        }));
        sortItems.add(new MenuItem("по названию: ", ()->{
            bookStore.getRequests(Comparator.comparing(request -> request.getBook().getTitle())).forEach(request -> {
                System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
            });
        }));
        sortItems.add(new MenuItem("назад", ()->{}));
        sortItems.add(new MenuItem("выход", ()->System.exit(0)));
        Menu sortMenu=new Menu("Меню сортировки", (ArrayList<MenuItem>) sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }

    private void displayeExecuteOrders(List<Order> orders){
        LocalDate startDate=action.readDate();
        LocalDate endDate=action.readDate();
        System.out.println("Сортировка заказов: ");
        List<MenuItem> sortItems=new ArrayList<>();
        sortItems.add(new MenuItem("по дате: ", ()->{
            bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getExecutionDate)).forEach(order -> {
                System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " +
                        order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
            });
        }));
        sortItems.add(new MenuItem("по цене: ", ()->{
            bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getOrderPrice)).forEach(order -> {
                System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " +
                        order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
            });
        }));
        sortItems.add(new MenuItem("назад", ()->{}));
        sortItems.add(new MenuItem("выход", ()->System.exit(0)));
        Menu sortMenu=new Menu("Меню сортировки", (ArrayList<MenuItem>) sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }

    private void displayOldBooksMenu(Map<String, Book> oldBooks){
        LocalDate thresholdDate = LocalDate.now().minusMonths(Util.getThresholdDateBook());
        System.out.println("Сортировка запросов: ");
        List<MenuItem> sortItems=new ArrayList<>();
        sortItems.add(new MenuItem("по дате публикации: ", ()->bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPublishDate))
                .forEach(System.out::println)));
        sortItems.add(new MenuItem("по цене: ", ()->bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPrice))
                .forEach(System.out::println)));
        sortItems.add(new MenuItem("назад", ()->{}));
        sortItems.add(new MenuItem("выход", ()->System.exit(0)));
        Menu sortMenu=new Menu("Меню сортировки", (ArrayList<MenuItem>) sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }
}

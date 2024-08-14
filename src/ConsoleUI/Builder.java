package ConsoleUI;

import BookStoreController.BookStoreController;
import BookStoreController.OrderController;
import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.Order;
import BookStoreModel.Request;
import Status.BookStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Builder {
    private BookStore bookStore;
    private BookStoreController bookStoreController;
    private OrderController orderController;
    private Navigator navigator;
    private IAction action;
    public Builder(BookStore bookStore, Navigator navigator){
        this.bookStore=bookStore;
        this.navigator=navigator;
        orderController=new OrderController(bookStore);
        bookStoreController=new BookStoreController(bookStore, orderController);
    }
    public Menu buildUserMenu(){
        MenuItem[] items={
                new MenuItem("Посмотреть все книги", ()-> displayBookMenu(bookStore.getBookInventory())),
                new MenuItem("Оформить заказ", ()-> {
                    String title=action.readBookTitle();
                    if(bookStore.getBookInventory().get(title)!=null)
                        bookStoreController.placeOrder(action.readBookTitle());
                    else
                        System.out.println("Книга с таким названием не найдена");
                }),
                new MenuItem("Посмотреть описание книги", ()->{
                    Book book=bookStore.getBookInventory().get(action.readBookTitle());
                    if(book!=null)
                        System.out.println(book.getDescription());
                    else
                        System.out.println("Книга с таким названием не найдена");
                }),
                new MenuItem("Выйти", ()->System.exit(0))
        };
        return new Menu("Меню пользователя", items);
    }
    public Menu buildAdminMenu(){
        MenuItem addBookMenuItem=new MenuItem("Добавить книгу", ()->{}, buildAddBookMenu());
        MenuItem removeBookMenuItem=new MenuItem("Удалить книгу", ()->{}, buildRemoveBookMenu());
        MenuItem[] items={
                new MenuItem("Посмотреть все книги", ()->displayBookMenu(bookStore.getBookInventory())),
                addBookMenuItem, removeBookMenuItem,
                new MenuItem("Посмотреть все заказы", ()->displayOrderMenu(bookStore.getOrders())),
                new MenuItem("Посмотреть все запросы", ()->displayRequestsMenu(bookStore.getRequests())),
                new MenuItem("Посмотреть все выполненные заказы за период", ()->displayeExecuteOrders(bookStore.getOrders())),
                new MenuItem("Посмотреть не проданный книги", ()->displayOldBooksMenu(bookStore.getBookInventory())),
                new MenuItem("Выйти", ()->System.exit(0))
        };
        return new Menu("Меню администратора", items);
    }
    private Menu buildAddBookMenu(){
        MenuItem[] items={
                new MenuItem("Данные книги", ()->bookStoreController.addBook(new Book(action.readBookTitle(), BookStatus.IN_STOCK,
                            action.readDate(), action.readPrice(), action.readDescription()))),
                new MenuItem("Назад", ()->{}),
                new MenuItem("Выйти", ()->System.exit(0))
        };
        return new Menu("Добавление книги", items);
    }
    private Menu buildRemoveBookMenu(){
        MenuItem[] items={
                new MenuItem("Название", ()->bookStoreController.removeBook(action.readBookTitle())),
                new MenuItem("Назад", ()->{}),
                new MenuItem("Выйти", ()->System.exit(0))
        };
        return new Menu("Удаление книги", items);
    }

    private void displayBookMenu(Map<String, Book> books){
        System.out.println("Сортировака книг: ");
        MenuItem[] sortItems={
                new MenuItem("по названию", ()->bookStore.getBookInventory(Comparator.comparing(Book::getTitle))
                        .forEach(System.out::println)),
                new MenuItem("по дате публикации: ", ()->bookStore.getBookInventory(Comparator.comparing(Book::getPublishDate))
                        .forEach(System.out::println)),
                new MenuItem("по цене: ", ()->bookStore.getBookInventory(Comparator.comparing(Book::getPrice))
                        .forEach(System.out::println)),
                new MenuItem("по наличию на складе", ()->bookStore.getBookInventory(Comparator.comparing(Book::getStatus))
                        .forEach(System.out::println)),
                new MenuItem("назад", ()->{}),
                new MenuItem("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }
    private void displayOrderMenu(List<Order> orders){
        System.out.println("Сортировка заказов: ");
        MenuItem[] sortItems={
                new MenuItem("дате исполнения: ", ()->{
                    bookStore.getOrders(Comparator.comparing(order -> order.getExecutionDate() != null ? order.getExecutionDate() : LocalDate.MAX))
                            .forEach(order -> {
                                System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Статус: " + order.getStatus());
                            });
                }),
                new MenuItem("по цене: ", ()->{
                    bookStore.getOrders(Comparator.comparing(order -> order.getOrderPrice()))
                            .forEach(order -> {
                                System.out.println("Книга: " + order.getBook().getTitle() + ", Цена заказа: " + order.getOrderPrice());
                            });
                }),
                new MenuItem("по статусу: ", ()->{
                    bookStore.getOrders(Comparator.comparing(order -> order.getStatus()))
                            .forEach(order -> {
                                System.out.println("Книга: " + order.getBook().getTitle() + ", Статус заказа" + order.getStatus());
                            });
                }),
                new MenuItem("назад", ()->{}),
                new MenuItem("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }
    private void displayRequestsMenu(List<Request> requests){
        System.out.println("Сортировка запросов: ");
        MenuItem[] sortItems={
                new MenuItem("по количеству запросов: ", ()->{
                    bookStore.getRequests(Comparator.comparingInt(Request::getRequestCount).reversed()).forEach(request -> {
                        System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
                    });
                }),
                new MenuItem("по названию: ", ()->{
                        bookStore.getRequests(Comparator.comparing(request -> request.getBook().getTitle())).forEach(request -> {
                            System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
                        });
                }),
                new MenuItem("назад", ()->{}),
                new MenuItem("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }
    private void displayeExecuteOrders(List<Order> orders){
        LocalDate startDate=action.readDate();
        LocalDate endDate=action.readDate();
        System.out.println("Сортировка заказов: ");
        MenuItem[] sortItems={
                new MenuItem("по дате: ", ()->{
                    bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getExecutionDate)).forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
                    });
                }),
                new MenuItem("по цене: ", ()->{
                    bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getOrderPrice)).forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
                    });
                }),
                new MenuItem("назад", ()->{}),
                new MenuItem("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }
    private void displayOldBooksMenu(Map<String, Book> oldBooks){
        LocalDate thresholdDate = LocalDate.now().minusMonths(6);
        System.out.println("Сортировка запросов: ");
        MenuItem[] sortItems={
                new MenuItem("по дате публикации: ", ()->bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPublishDate))
                        .forEach(System.out::println)),
                new MenuItem("по цене: ", ()->bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPrice))
                        .forEach(System.out::println)),
                new MenuItem("назад", ()->{}),
                new MenuItem("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu();
        navigator.navigate(sortMenu);
    }
}

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
    public Builder(BookStore bookStore, Navigator navigator){
        this.bookStore=bookStore;
        this.navigator=navigator;
        orderController=new OrderController(bookStore);
        bookStoreController=new BookStoreController(bookStore, orderController);
    }
    public Menu buildUserMenu(){
        MenuItems[] items={
                new MenuItems("Посмотреть все книги", ()-> displayBookMenu(bookStore.getBookInventory())),
                new MenuItems("Оформить заказ", ()-> {
                    String title=navigator.readBookTitle();
                    if(bookStore.getBookInventory().get(title)!=null)
                        bookStoreController.placeOrder(navigator.readBookTitle());
                    else
                        System.out.println("Книга с таким названием не найдена");
                }),
                new MenuItems("Посмотреть описание книги", ()->{
                    Book book=bookStore.getBookInventory().get(navigator.readBookTitle());
                    if(book!=null)
                        System.out.println(book.getDescription());
                    else
                        System.out.println("Книга с таким названием не найдена");
                }),
                new MenuItems("Выйти", ()->System.exit(0))
        };
        return new Menu("Меню пользователя", items);
    }
    public Menu buildAdminMenu(){
        MenuItems addBookMenuItem=new MenuItems("Добавить книгу", ()->{}, buildAddBookMenu(), navigator);
        MenuItems removeBookMenuItem=new MenuItems("Удалить книгу", ()->{}, buildRemoveBookMenu(), navigator);
        MenuItems[] items={
                new MenuItems("Посмотреть все книги", ()->displayBookMenu(bookStore.getBookInventory())), addBookMenuItem, removeBookMenuItem,
                new MenuItems("Посмотреть все заказы", ()->displayOrderMenu(bookStore.getOrders())),
                new MenuItems("Посмотреть все запросы", ()->displayRequestsMenu(bookStore.getRequests())),
                new MenuItems("Посмотреть все выполненные заказы за период", ()->displayeExecuteOrders(bookStore.getOrders())),
                new MenuItems("Посмотреть не проданный книги", ()->displayOldBooksMenu(bookStore.getBookInventory())),
                new MenuItems("Выйти", ()->System.exit(0))
        };
        return new Menu("Меню администратора", items);
    }
    private Menu buildAddBookMenu(){
        MenuItems[] items={
                new MenuItems("Данные книги", ()->bookStoreController.addBook(new Book(navigator.readBookTitle(), BookStatus.IN_STOCK,
                        navigator.readDate(), navigator.readPrice(), navigator.readDescription()))),
                new MenuItems("Назад", ()->{}),
                new MenuItems("Выйти", ()->System.exit(0))
        };
        return new Menu("Добавление книги", items);
    }
    private Menu buildRemoveBookMenu(){
        MenuItems[] items={
                new MenuItems("Название", ()->bookStoreController.removeBook(navigator.readBookTitle())),
                new MenuItems("Назад", ()->{}),
                new MenuItems("Выйти", ()->System.exit(0))
        };
        return new Menu("Удаление книги", items);
    }

    private void displayBookMenu(Map<String, Book> books){
        System.out.println("Сортировака книг: ");
        MenuItems[] sortItems={
                new MenuItems("по названию", ()->bookStore.getBookInventory(Comparator.comparing(Book::getTitle))
                        .forEach(System.out::println)),
                new MenuItems("по дате публикации: ", ()->bookStore.getBookInventory(Comparator.comparing(Book::getPublishDate))
                        .forEach(System.out::println)),
                new MenuItems("по цене: ", ()->bookStore.getBookInventory(Comparator.comparing(Book::getPrice))
                        .forEach(System.out::println)),
                new MenuItems("по наличию на складе", ()->bookStore.getBookInventory(Comparator.comparing(Book::getStatus))
                        .forEach(System.out::println)),
                new MenuItems("назад", ()->{}),
                new MenuItems("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu(sortMenu);
        navigator.navigate(sortMenu);
    }
    private void displayOrderMenu(List<Order> orders){
        System.out.println("Сортировка заказов: ");
        MenuItems[] sortItems={
                new MenuItems("дате исполнения: ", ()->{
                    bookStore.getOrders(Comparator.comparing(order -> order.getExecutionDate() != null ? order.getExecutionDate() : LocalDate.MAX))
                            .forEach(order -> {
                                System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Статус: " + order.getStatus());
                            });
                }),
                new MenuItems("по цене: ", ()->{
                    bookStore.getOrders(Comparator.comparing(order -> order.getOrderPrice()))
                            .forEach(order -> {
                                System.out.println("Книга: " + order.getBook().getTitle() + ", Цена заказа: " + order.getOrderPrice());
                            });
                }),
                new MenuItems("по статусу: ", ()->{
                    bookStore.getOrders(Comparator.comparing(order -> order.getStatus()))
                            .forEach(order -> {
                                System.out.println("Книга: " + order.getBook().getTitle() + ", Статус заказа" + order.getStatus());
                            });
                }),
                new MenuItems("назад", ()->{}),
                new MenuItems("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu(sortMenu);
        navigator.navigate(sortMenu);
    }
    private void displayRequestsMenu(List<Request> requests){
        System.out.println("Сортировка запросов: ");
        MenuItems[] sortItems={
                new MenuItems("по количеству запросов: ", ()->{
                    bookStore.getRequests(Comparator.comparingInt(Request::getRequestCount).reversed()).forEach(request -> {
                        System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
                    });
                }),
                new MenuItems("по названию: ", ()->{
                        bookStore.getRequests(Comparator.comparing(request -> request.getBook().getTitle())).forEach(request -> {
                            System.out.println("Книга: " + request.getBook().getTitle() + ", Колличество запросов: " + request.getRequestCount());
                        });
                }),
                new MenuItems("назад", ()->{}),
                new MenuItems("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu(sortMenu);
        navigator.navigate(sortMenu);
    }
    private void displayeExecuteOrders(List<Order> orders){
        LocalDate startDate=navigator.readDate();
        LocalDate endDate=navigator.readDate();
        System.out.println("Сортировка заказов: ");
        MenuItems[] sortItems={
                new MenuItems("по дате: ", ()->{
                    bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getExecutionDate)).forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
                    });
                }),
                new MenuItems("по цене: ", ()->{
                    bookStore.getFullfilledOrders(startDate, endDate, Comparator.comparing(Order::getOrderPrice)).forEach(order -> {
                        System.out.println("Книга: " + order.getBook().getTitle() + ", Дата заказа: " + order.getExecutionDate() + ", Цена: " + order.getBook().getPrice());
                    });
                }),
                new MenuItems("назад", ()->{}),
                new MenuItems("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu(sortMenu);
        navigator.navigate(sortMenu);
    }
    private void displayOldBooksMenu(Map<String, Book> oldBooks){
        LocalDate thresholdDate = LocalDate.now().minusMonths(6);
        System.out.println("Сортировка запросов: ");
        MenuItems[] sortItems={
                new MenuItems("по дате публикации: ", ()->bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPublishDate))
                        .forEach(System.out::println)),
                new MenuItems("по цене: ", ()->bookStore.getOldBooks(thresholdDate, Comparator.comparing(Book::getPrice))
                        .forEach(System.out::println)),
                new MenuItems("назад", ()->{}),
                new MenuItems("выход", ()->System.exit(0))
        };
        Menu sortMenu=new Menu("Меню сортировки", sortItems);
        navigator.printMenu(sortMenu);
        navigator.navigate(sortMenu);
    }
}

package ConsoleUI;

import BookStoreModel.BookStore;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Stack;

public class Navigator {
    private BookStore bookStore;
    private Stack<Menu> menuStack;
    public Navigator(BookStore bookStore){
        this.bookStore=bookStore;
        this.menuStack=new Stack<Menu>();
    }
    public void printMenu(Menu menu){
        menuStack.push(menu);
        System.out.println(menu.getTitle());
        for(int i=0;i<menu.getItems().length;i++)
            System.out.println((i+1)+". "+menu.getItems()[i].getTitle());
    }
    public void navigate(Menu menu){
        while (true) {
            System.out.print("Выберите пункт меню: ");
            try {
                int choice = Integer.parseInt(System.console().readLine());
                if (choice < 1 || choice > menu.getItems().length)
                    System.out.println("Некорректный ввод.");
                else {
                    MenuItems selectedItem = menu.getItems()[choice - 1];
                    selectedItem.doAction();
                    if (menuStack.size() > 1) {
                        menuStack.pop();
                        printMenu(menuStack.peek());
                    }
                }
            }catch (NumberFormatException e){
                System.out.println("Некорректный формат ввода");
            }
        }
    }
    public String readBookTitle(){
        System.out.print("Введите название книги: ");
        String input = System.console().readLine();
        if(input.length()>0)
            return input;
        else
            return "Название книги не может быть пустым";
    }
    public LocalDate readDate(){
        LocalDate date;
        int year, month, day;
        try{
        System.out.print("Введите год: ");
        year = Integer.parseInt(System.console().readLine());
        System.out.print("Введите месяц: ");
        month = Integer.parseInt(System.console().readLine());
        System.out.print("Введите день: ");
        day =   Integer.parseInt(System.console().readLine());
        date=LocalDate.of(year, month, day);
            return date;
        }catch (NullPointerException e){
            System.out.println("Некорректный формат ввода");
            return null;
        }
    }
    public int readPrice(){
        System.out.print("Введите цену книги: ");
        return Integer.parseInt(System.console().readLine());
    }
    public String readDescription(){
        System.out.print("Введите описание книги: ");
        return System.console().readLine();
    }
}

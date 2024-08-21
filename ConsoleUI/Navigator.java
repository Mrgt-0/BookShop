package ConsoleUI;

import BookStoreModel.BookStore;

import java.time.LocalDate;
import java.util.EmptyStackException;
import java.util.InputMismatchException;
import java.util.Stack;

public class Navigator {
    private Menu currentMenu;
    private Navigator navigator;
    public Navigator(){
        currentMenu=new Menu("Главное меню", new MenuItem[0]);
        this.navigator=this;
    }
    public void printMenu(){
        System.out.println(currentMenu.getTitle());
        for (int i = 0; i < currentMenu.getItems().length; i++)
            System.out.println((i + 1) + ". " + currentMenu.getItems()[i].getTitle());

    }
    public void navigate(Menu menu){
        this.currentMenu=menu;
        while (true) {
            printMenu();
            System.out.print("Выберите пункт меню: ");
            try {
                int choice = Integer.parseInt(System.console().readLine());
                if (choice < 1 || choice > menu.getItems().length)
                    System.out.println("Некорректный ввод.");
                else {
                    MenuItem selectedItem = menu.getItems()[choice - 1];
                    selectedItem.doAction();
                    if(selectedItem.getTitle().equals("Выйти"))
                        System.exit(0);
                }
            }catch (NumberFormatException e){
                System.out.println("Некорректный формат ввода");
            }
        }
    }
}

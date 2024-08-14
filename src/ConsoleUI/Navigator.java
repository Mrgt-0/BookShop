package ConsoleUI;

import BookStoreModel.BookStore;

import java.time.LocalDate;
import java.util.EmptyStackException;
import java.util.InputMismatchException;
import java.util.Stack;

public class Navigator {
    private Stack<Menu> menuStack;
    private Menu currentMenu;
    public Navigator(){
        this.menuStack=new Stack<Menu>();
        currentMenu=new Menu("Главное меню", new MenuItem[0]);
    }
    public void printMenu(){
        if(this.currentMenu!=null) {
            menuStack.push(currentMenu);
            System.out.println(currentMenu.getTitle());
            for (int i = 0; i < currentMenu.getItems().length; i++)
                System.out.println((i + 1) + ". " + currentMenu.getItems()[i].getTitle());
        }
    }
    public void navigate(Menu menu){
        menuStack.push(menu);
        this.currentMenu=menu;
        printMenu();
        while (true) {
            System.out.print("Выберите пункт меню: ");
            try {
                int choice = Integer.parseInt(System.console().readLine());
                if (choice < 1 || choice > menu.getItems().length)
                    System.out.println("Некорректный ввод.");
                else {
                    MenuItem selectedItem = menu.getItems()[choice - 1];
                    selectedItem.doAction();
                    if (menuStack.size() > 1) {
                        menuStack.pop();
                        if(!menuStack.isEmpty())
                            currentMenu = menuStack.peek();
                        else
                            this.currentMenu=menu;
                        printMenu();
                    }
                }
            }catch (NumberFormatException e){
                System.out.println("Некорректный формат ввода");
            }
        }
    }
}

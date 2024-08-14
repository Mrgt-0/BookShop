package ConsoleUI;

import java.util.Scanner;

public class BuilderController {
    private Builder builder;
    private Navigator navigator;
    public BuilderController(Builder builder, Navigator navigator){
        this.builder=builder;
        this.navigator=navigator;
    }
    public void run(){
        Menu menu = isAdmin() ? builder.buildAdminMenu() : builder.buildUserMenu();
        navigator.printMenu();
        navigator.navigate(menu);
    }
    private boolean isAdmin() {
        System.out.print("Введи 1 если вы администратор\n" +
                "Введи 2 если вы пользователь\n" +
                "Введите цифру: ");
        boolean isAdminOrNot = false;
        boolean isRight = false;
        do {
            try {
                int input = Integer.parseInt(System.console().readLine());
                if (input == 1)
                    isAdminOrNot = true;
                else if (input == 2)
                    isAdminOrNot = false;
                isRight=true;
            }catch(NumberFormatException e){
                System.out.println("Некорректный формат ввода");
            }
        } while (!isRight);
        return isAdminOrNot;
    }
}

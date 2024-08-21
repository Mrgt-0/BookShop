package ConsoleUI;

import java.time.LocalDate;

public class ActionHelper {
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

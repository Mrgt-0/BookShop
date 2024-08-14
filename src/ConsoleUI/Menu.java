package ConsoleUI;

public class Menu {
    private MenuItems[] items;
    private String title;
    public Menu(String title, MenuItems[] items){
        this.title=title;
        this.items=items;
    }
    public String getTitle(){
        return title;
    }
    public MenuItems[] getItems(){
        return items;
    }
}

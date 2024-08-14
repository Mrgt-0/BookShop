package ConsoleUI;

public class Menu {
    private MenuItem[] items;
    private String title;
    public Menu(String title, MenuItem[] items){
        this.title=title;
        this.items=items;
    }
    public String getTitle(){
        return title;
    }
    public MenuItem[] getItems(){
        return items;
    }
}

package ConsoleUI;

import java.util.ArrayList;

public class Menu {
    private ArrayList<MenuItem> items;
    private String title;

    public Menu(String title, ArrayList<MenuItem> items){
        this.title=title;
        this.items=items;
    }

    public String getTitle(){
        return title;
    }

    public ArrayList<MenuItem> getItems(){
        return items;
    }
}

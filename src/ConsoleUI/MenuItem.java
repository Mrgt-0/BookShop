package ConsoleUI;

public class MenuItem {
    private final String title;
    private final IAction action;
    private final Menu nextMenu;
    public MenuItem(String title, IAction action){
        this.title=title;
        this.action=action;
        this.nextMenu=null;
    }
    public MenuItem(String title, IAction action, Menu nextMenu){
        this.title=title;
        this.action=action;
        this.nextMenu=nextMenu;
    }
    public String getTitle(){
        return title;
    }
    public void doAction(){
        action.execute();
    }
}

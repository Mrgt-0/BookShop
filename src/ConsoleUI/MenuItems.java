package ConsoleUI;

public class MenuItems {
    private String title;
    private IAction action;
    private Menu nextMenu;
    Navigator navigator;
    public MenuItems(String title, IAction action){
        this.title=title;
        this.action=action;
        this.nextMenu=null;
    }
    public MenuItems(String title, IAction action, Menu nextMenu, Navigator navigator){
        this.title=title;
        this.action=action;
        this.nextMenu=nextMenu;
        this.navigator=navigator;
    }
    public String getTitle(){
        return title;
    }
    public void doAction(){
        if(nextMenu!=null){
            navigator.printMenu(nextMenu);
            navigator.navigate(nextMenu);
        }else
            action.execute();
    }
}

package ylab.menu;

public interface IMenu {

    void display();

    void handleInput(int choice);

    void backMenu();

    void backMainMenu();

    void pushMenu(IMenu menu);

    void exit();
}

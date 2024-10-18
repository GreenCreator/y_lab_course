package ylab.menu;

public interface IMenu {

    /**
     * Displaying general information on the screen
     */
    void display();

    /**
     * Input processing
     * @param choice - user entered value
     */
    void handleInput(int choice);

    /**
     * Go back in the hierarchy to the menu
     */
    void backMenu();

    /**
     * Go back main menu
     */
    void backMainMenu();

    /**
     * Open submenu
     */
    void pushMenu(IMenu menu);

    /**
     * exit the program
     */
    void exit();
}

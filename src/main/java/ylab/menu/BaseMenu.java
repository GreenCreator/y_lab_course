package ylab.menu;


import java.util.Scanner;

public abstract class BaseMenu implements IMenu {
    private final MenuManager menuManager;
    private Scanner scanner;

    protected BaseMenu(MenuManager menuManager, Scanner scanner) {
        this.menuManager = menuManager;
        this.scanner = scanner;
    }

    protected MenuManager getMenuManager() {
        return menuManager;
    }

    protected Scanner getScanner() {
        return scanner;
    }

    @Override
    public void backMenu() {
        menuManager.popMenu();
    }

    @Override
    public void backMainMenu() {
        menuManager.popToMainMenu();
    }

    @Override
    public void pushMenu(IMenu menu) {
        menuManager.pushMenu(menu);
    }

    @Override
    public void exit() {
        menuManager.exit();
    }
}

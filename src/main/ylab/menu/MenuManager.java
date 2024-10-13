package ylab.menu;

import java.util.Scanner;
import java.util.Stack;

public class MenuManager {
    private Stack<IMenu> menuStack = new Stack<>();
    private boolean running = true;

    public void pushMenu(IMenu menu) {
        menuStack.push(menu);
    }

    public void popMenu() {
        if (!menuStack.isEmpty()) {
            menuStack.pop();
        }
    }

    public void popToMainMenu() {
        while (menuStack.size() > 1) {
            menuStack.pop();
        }
    }

    public void exit() {
        running = false;
    }

    public void start() {
        while (running && !menuStack.isEmpty()) {
            IMenu currentMenu = menuStack.peek();
            currentMenu.display();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            currentMenu.handleInput(choice);
        }
    }
}


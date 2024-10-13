package ylab.menu.user;

import ylab.entity.user.User;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;
import ylab.menu.habit.HabitMenu;
import ylab.menu.habit.HabitTrackingMenu;
import ylab.utils.UserManager;

import java.util.Scanner;

public class UserMenu extends BaseMenu {
    private User user;
    private UserManager userManager;

    public UserMenu(User user, UserManager userManager, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.user = user;
        this.userManager = userManager;
    }

    @Override
    public void display() {
        System.out.println("Welcome, " + user.getName());
        System.out.println("1. Manage Habits");
        System.out.println("2. Tracking Habits");
        System.out.println("3. Setting");
        System.out.println("4. Logout");
    }

    @Override
    public void handleInput(int choice) {
        var scanner = getScanner();
        switch (choice) {
            case 1:
                pushMenu(new HabitMenu(user, getMenuManager(), scanner));
                break;
            case 2:
                pushMenu(new HabitTrackingMenu(user, getMenuManager(), scanner));
                break;
            case 3:
                pushMenu(new UserSettingMenu(user, userManager, getMenuManager(), scanner));
                break;
            case 4:
                System.out.println("Logged out.");
                backMenu();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

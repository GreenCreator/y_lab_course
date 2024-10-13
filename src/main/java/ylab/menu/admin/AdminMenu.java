package ylab.menu.admin;

import ylab.entity.admin.Admin;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;
import ylab.utils.UserManager;

import java.util.Scanner;

public class AdminMenu extends BaseMenu {
    private Admin admin;
    private UserManager userManager;

    public AdminMenu(Admin admin, UserManager userManager, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.admin = admin;
        this.userManager = userManager;
    }

    @Override
    public void display() {
        System.out.println("Welcome to admin menu, " + admin.getName());
        System.out.println("1. View all users");
        System.out.println("2. Block user");
        System.out.println("3. Delete user");
        System.out.println("4. Logout");
    }

    @Override
    public void handleInput(int choice) {
        Scanner scanner = getScanner();
        switch (choice) {
            case 1:
                admin.listAllUsers();
                break;
            case 2:
                System.out.print("Enter email user: ");
                String emailBlock = scanner.nextLine();
                userManager.blockUser(emailBlock);
                break;
            case 3:
                System.out.print("Enter email user: ");
                String emailDelete = scanner.nextLine();
                admin.deleteUser(emailDelete);
                if (emailDelete.equals(admin.getEmail())) {
                    backMainMenu();
                }
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

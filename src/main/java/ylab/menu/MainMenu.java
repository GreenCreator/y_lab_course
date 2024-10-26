package ylab.menu;

import ylab.ConnectRepo;
import ylab.entity.admin.Admin;
import ylab.menu.admin.AdminMenu;
import ylab.menu.user.UserMenu;
import ylab.utils.UserManager;

import java.util.Scanner;

public class MainMenu extends BaseMenu {
    private UserManager userManager;
    private ConnectRepo connection;

    public MainMenu(UserManager userManager, MenuManager menuManager, Scanner scanner, ConnectRepo connection) {
        super(menuManager, scanner);
        this.userManager = userManager;
        this.connection = connection;
    }

    @Override
    public void display() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    @Override
    public void handleInput(int choice) {
        var scanner = getScanner();
        switch (choice) {
            case 1:
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                System.out.print("Is admin account? (yes/no): ");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    userManager.registerAdmin(name, email, password);
                } else {
                    userManager.registerUser(name, email, password);
                }
                break;
            case 2:
                System.out.print("Enter email: ");
                String loginEmail = scanner.nextLine();
                System.out.print("Enter password: ");
                String loginPassword = scanner.nextLine();
                var user = userManager.login(loginEmail, loginPassword);
                if (user == null) {
                    System.out.println("Login failed!");
                    break;
                }
                if (user.GetBlockedStatus()) {
                    System.out.println("You are blocked!");
                    break;
                }

                if (user.isAdmin()) {
                    pushMenu(new AdminMenu(new Admin(user.getName(), user.getEmail(), user.getPassword(), userManager), userManager, getMenuManager(), scanner));
                } else {
                    pushMenu(new UserMenu(user, userManager, getMenuManager(), scanner));
                }
                break;
            case 3:
                connection.closeConnection();
                exit();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}


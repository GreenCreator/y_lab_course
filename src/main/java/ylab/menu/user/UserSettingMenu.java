package ylab.menu.user;

import ylab.entity.user.User;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;
import ylab.utils.UserManager;

import java.util.Scanner;

public class UserSettingMenu extends BaseMenu {

    private User user;
    private UserManager userManager;

    public UserSettingMenu(User user, UserManager userManager, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.user = user;
        this.userManager = userManager;
    }

    @Override
    public void display() {
        System.out.println("\n");
        System.out.println("===== Settings =====\n");
        System.out.println("1. Change name");
        System.out.println("2. Change email");
        System.out.println("3. Change password");
        System.out.println("4. Delete user");
        System.out.println("5. Back");
    }

    @Override
    public void handleInput(int choice) {
        var scanner = getScanner();
        switch (choice) {
            case 1:
                System.out.print("Current name: " + user.getName() + "\n");
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                user.setName(name);
                userManager.updateNameUser(user);
                break;
            case 2:
                System.out.print("Current email: " + user.getEmail() + "\n");
                System.out.print("Enter new email: ");
                String email = scanner.nextLine();
                user.setEmail(email);
                userManager.updateEmailUser(user);
                break;
            case 3:
                System.out.print("Current password: " + user.getPassword() + "\n");
                System.out.print("Enter new password: ");
                String password = scanner.nextLine();
                user.setPassword(password);
                userManager.updatePasswordUser(user);
                break;
            case 4:
                System.out.print("Are you sure you want to delete your account? (yes/no): ");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("yes")) {
                    userManager.deleteUser(user.getEmail());
                    System.out.println("Returning to main menu...");
                    backMainMenu();
                }
                break;
            case 5:
                backMenu();
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

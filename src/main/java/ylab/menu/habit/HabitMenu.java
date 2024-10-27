package ylab.menu.habit;

import ylab.entity.user.User;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;

import java.util.Scanner;

public class HabitMenu extends BaseMenu {
    private User user;

    public HabitMenu(User user, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.user = user;
    }

    @Override
    public void display() {
        System.out.println("\n");
        System.out.println("===== Habits =====\n");
        System.out.println("1. Create Habit");
        System.out.println("2. Edit Habit");
        System.out.println("3. Delete Habit");
        System.out.println("4. View Habits");
        System.out.println("5. Back");
    }

    @Override
    public void handleInput(int choice) {
        String title;
        Scanner scanner = getScanner();
        var habitManager = user.getHabitManager();
        switch (choice) {
            case 1:
                System.out.print("Enter habit title: ");
                title = scanner.nextLine();
                System.out.print("Enter description: ");
                String description = scanner.nextLine();
                System.out.print("Enter frequency (daily/weekly): ");
                String frequency = scanner.nextLine();
                habitManager.createHabit(title, description, frequency, user.getId());
                break;
            case 2:
                System.out.print("Enter habit title: ");
                title = scanner.nextLine();
                var habit = habitManager.getHabitByTitle(title);
                if (habit != null) {
                    System.out.print("Enter new description for habit: " + habit.getTitle() + "\n");
                    String newDescription = scanner.nextLine();
                    habitManager.updateDescriptionByTitle(title, newDescription);
                } else {
                    System.out.print("Not found habit\n");
                }
                break;
            case 3:
                System.out.print("Enter habit title: ");
                title = scanner.nextLine();
                habitManager.deleteHabit(title);
                break;
            case 4:
                pushMenu(new HabitViewMenu(user, getMenuManager(), scanner));
                break;
            case 5:
                backMenu();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

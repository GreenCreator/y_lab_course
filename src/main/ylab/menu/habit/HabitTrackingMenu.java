package ylab.menu.habit;

import ylab.entity.user.User;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;
import ylab.utils.HabitStatistics;

import java.time.LocalDate;
import java.util.Scanner;

public class HabitTrackingMenu extends BaseMenu {
    private User user;

    public HabitTrackingMenu(User user, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.user = user;
    }

    @Override
    public void display() {
        System.out.println("\n");
        System.out.println("======Habit Tracking=====\n");
        System.out.println("1. Mark Habit Complete");
        System.out.println("2. Generate Habit Report");
        System.out.println("3. Generating a progress report for the user");
        System.out.println("4. Back");
    }

    @Override
    public void handleInput(int choice) {
        Scanner scanner = getScanner();
        switch (choice) {
            case 1:
                System.out.print("Enter habit name to mark complete: ");
                String habitName = scanner.nextLine();
                user.getHabitManager().getHabitByTitle(habitName).markCompleted();
                break;
            case 2:
                System.out.println("Enter the habit title: ");
                String habitTitle = scanner.nextLine();

                var habit = user.getHabitManager().getHabitByTitle(habitTitle);
                if (habit != null) {
                    pushMenu(new HabitReportMenu(habit, getMenuManager(), scanner));
                }
                break;

            case 3:
                System.out.println("Enter start date (YYYY-MM-DD): ");
                LocalDate startDate = LocalDate.parse(scanner.nextLine());

                System.out.println("Enter end date (YYYY-MM-DD): ");
                LocalDate endDate = LocalDate.parse(scanner.nextLine());

                HabitStatistics.generateProgressReport(user, startDate, endDate);
                break;
            case 4:
                backMenu();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

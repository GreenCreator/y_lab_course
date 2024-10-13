package ylab.menu.habit;

import ylab.entity.habit.Habit;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;
import ylab.utils.HabitStatistics;

import java.time.LocalDate;
import java.util.Scanner;

public class HabitReportMenu extends BaseMenu {
    private Habit habit;

    public HabitReportMenu(Habit habit, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.habit = habit;
    }

    @Override
    public void display() {
        System.out.println("Choose the period to view statistics:");
        System.out.println("1. Day");
        System.out.println("2. Week");
        System.out.println("3. Month");
        System.out.println("4. Counting the current completion of habit series");
        System.out.println("5. Percentage of successful completion of habits over a certain period");
        System.out.println("6. Back");
    }

    @Override
    public void handleInput(int choice) {
        Scanner scanner = getScanner();
        LocalDate today = LocalDate.now();
        switch (choice) {
            case 1:
                long dayCount = HabitStatistics.getCompletionCountForDay(habit, today);
                System.out.println("Habit completed " + dayCount + " times today.");
                break;
            case 2:
                long weekCount = HabitStatistics.getCompletionCountForWeek(habit, today.minusDays(7));
                System.out.println("Habit completed " + weekCount + " times in the last week.");
                break;
            case 3:
                long monthCount = HabitStatistics.getCompletionCountForMonth(habit, today.withDayOfMonth(1));
                System.out.println("Habit completed " + monthCount + " times in the last month.");
                break;
            case 4:
                int streak = HabitStatistics.getCurrentStreak(habit);
                System.out.println("Current streak for habit '" + habit.getTitle() + "': " + streak + " days.");
                break;
            case 5:
                System.out.println("Enter start date (YYYY-MM-DD): ");
                LocalDate startDate = LocalDate.parse(scanner.nextLine());

                System.out.println("Enter end date (YYYY-MM-DD): ");
                LocalDate endDate = LocalDate.parse(scanner.nextLine());

                double successPercentage = HabitStatistics.getSuccessPercentage(habit, startDate, endDate);
                System.out.println("Success percentage for habit '" + habit.getTitle() + "' in the period: " + successPercentage + "%");

                break;
            case 6:
                backMenu();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

package ylab.menu.habit;

import ylab.entity.habit.Habit;
import ylab.entity.user.User;
import ylab.menu.BaseMenu;
import ylab.menu.MenuManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HabitViewMenu extends BaseMenu {
    private User user;

    public HabitViewMenu(User user, MenuManager menuManager, Scanner scanner) {
        super(menuManager, scanner);
        this.user = user;
    }

    @Override
    public void display() {
        System.out.println("View habits:");
        System.out.println("1. By creation date");
        System.out.println("2. By status (completed or not)");
        System.out.println("3. Show all habits");
        System.out.println("4. Back");
    }

    @Override
    public void handleInput(int choice) {
        switch (choice) {
            case 1:
                filterByDate();
                break;
            case 2:
                filterByStatus();
                break;
            case 3:
                showAllHabits();
                break;
            case 4:
                backMenu();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void filterByDate() {
        System.out.print("Enter date (YYYY-MM-DD) to filter habits created after this date: ");
        String dateInput = getScanner().nextLine();
        LocalDate filterDate = LocalDate.parse(dateInput); // Преобразуем ввод в дату

        List<Habit> filteredHabits = user.getHabitManager().listHabits().stream()
                .filter(habit -> habit.getCreationDate().isAfter(filterDate))
                .collect(Collectors.toList());

        displayFilteredHabits(filteredHabits);
    }

    private void filterByStatus() {
        System.out.print("Enter status to filter by (completed/not completed): ");
        String statusInput = getScanner().nextLine().toLowerCase();

        boolean isCompleted = statusInput.equals("completed");

        List<Habit> filteredHabits = user.getHabitManager().listHabits().stream()
                .filter(habit -> habit.isCompleted() == isCompleted)
                .collect(Collectors.toList());

        displayFilteredHabits(filteredHabits);
    }

    private void showAllHabits() {
        List<Habit> allHabits = user.getHabitManager().listHabits();
        displayFilteredHabits(allHabits);
    }

    protected void displayFilteredHabits(List<Habit> habits) {
        if (habits.isEmpty()) {
            System.out.println("No habits found with the specified filter.");
        } else {
            habits.forEach(habit -> {
                System.out.println("Habit: " + habit.getTitle() +
                        ", Description: " + habit.getDescription() +
                        ", Created: " + habit.getCreationDate() +
                        ", Status: " + (habit.isCompleted() ? "Completed" : "Not completed"));
            });
        }
    }
}
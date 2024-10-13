package ylab.utils;

import ylab.entity.habit.Habit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class HabitManager {
    private List<Habit> habits = new ArrayList<>();

    public void createHabit(String title, String description, String frequency) {
        Habit habit = new Habit(title, description, frequency);
        habits.add(habit);
        System.out.println("Habit created.");
    }

    public void deleteHabit(String title) {
        Habit habit = findHabitById(title);
        if (habit != null) {
            habits.remove(habit);
            System.out.println("Habit deleted.");
        } else {
            System.out.println("Habit not found.");
        }
    }

    public List<Habit> listHabits() {
        return habits;
    }

    private Habit findHabitById(String title) {
        for (Habit habit : habits) {
            if (habit.getTitle().equals(title)) {
                return habit;
            }
        }
        return null;
    }


    public void editHabitDescription(String title) {
        boolean found = false;
        Scanner scanner = new Scanner(System.in);

        for (var habit : listHabits()) {
            if (habit.getTitle().equals(title)) {
                System.out.print("Enter new description for habit: " + habit.getTitle() + "\n");
                String newDescription = scanner.nextLine();
                habit.setDescription(newDescription);
                System.out.println("Completed\n");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.print("Not found habit\n");
        }
    }

    public Habit getHabitByTitle(String title) {
        Optional<Habit> habit = habits.stream()
                .filter(h -> h.getTitle().equalsIgnoreCase(title))
                .findFirst();

        return habit.orElse(null);
    }
}


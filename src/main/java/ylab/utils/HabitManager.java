package ylab.utils;

import ylab.entity.habit.Habit;

import java.util.HashMap;
import java.util.Map;

public class HabitManager {
    private Map<String, Habit> habits = new HashMap<>();

    public void createHabit(String title, String description, String frequency) {
        Habit habit = new Habit(title, description, frequency);
        habits.put(title, habit);
        System.out.println("Habit created.");
    }

    public void deleteHabit(String title) {
        Habit habit = getHabitByTitle(title);
        if (habit != null) {
            habits.remove(habit);
            System.out.println("Habit deleted.");
        } else {
            System.out.println("Habit not found.");
        }
    }

    public Map<String, Habit> listHabits() {
        return habits;
    }

    public Habit getHabitByTitle(String title) {
        return habits.get(title);
    }
}


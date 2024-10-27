package ylab.utils;

import ylab.entity.habit.Habit;
import ylab.impl.HabitRepositoryImpl;

import java.time.LocalDate;
import java.util.Map;

public class HabitManager {

    private HabitRepositoryImpl habitRepository;

    public HabitManager(HabitRepositoryImpl habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void createHabit(String title, String description, String frequency, long userId) {
        habitRepository.save(new Habit(1, title, description, frequency, false, LocalDate.now(), userId));
        System.out.println("Habit created and saved to database.");
    }

    public void deleteHabit(String title) {
        Habit habit = habitRepository.findByTitle(title);
        if (habit != null) {
            habitRepository.deleteById(habit.getId());
            System.out.println("Habit deleted from database.");
        } else {
            System.out.println("Habit not found.");
        }
    }

    public Map<Long, Habit> listHabits(long user_id) {
        return habitRepository.findAll(user_id);
    }

    public Habit getHabitByTitle(String title) {
        return habitRepository.findByTitle(title);
    }

    public void updateDescriptionByTitle(String title, String newDescription) {
        habitRepository.updateDescriptionByTitle(title, newDescription);
    }
}

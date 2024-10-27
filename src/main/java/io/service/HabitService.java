package io.service;

import io.annotations.AuditAction;
import io.annotations.Loggable;
import io.dto.HabitDTO;
import ylab.entity.habit.Habit;
import ylab.impl.HabitRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class HabitService {
    private final HabitRepositoryImpl habitRepository;

    public HabitService(HabitRepositoryImpl habitRepository) {
        this.habitRepository = habitRepository;
    }

    @AuditAction("Habit created")
    @Loggable
    public void createHabit(HabitDTO habitDTO) {
        // Преобразование HabitDTO в Habit
        Habit habit = new Habit(habitDTO.getId(), habitDTO.getTitle(), habitDTO.getDescription(), habitDTO.getFrequency(),
                habitDTO.isCompleted(), habitDTO.getCreationDate(), habitDTO.getUserId());

        habitRepository.save(habit);
        System.out.println("Habit created successfully: " + habit);
    }

    @Loggable
    public HabitDTO getHabitByTitle(String title) {
        Habit habit = null;
        habit = habitRepository.findByTitle(title);

        if (habit != null) {
            return new HabitDTO(habit.getId(), habit.getTitle(), habit.getDescription(), habit.getFrequency(),
                    habit.isCompleted(), habit.getCreationDate(), habit.getUserId());
        }

        return null; // Возвращаем null, если привычка не найдена
    }

    @Loggable
    public List<HabitDTO> getAllHabits(Long userId) {
        List<HabitDTO> habitsDTO = null;
        var habits = habitRepository.findAll(userId); // Получение всех привычек
        habitsDTO = habits.values().stream().map(habit -> new HabitDTO(habit.getId(), habit.getTitle(), habit.getDescription(), habit.getFrequency(),
                        habit.isCompleted(), habit.getCreationDate(), habit.getUserId()))
                .collect(Collectors.toList());

        return habitsDTO;
    }

    @AuditAction("Habit deleted")
    @Loggable
    public void deleteHabitById(Long id) {
        habitRepository.deleteById(id); // Удаление привычки по ID
        System.out.println("Habit deleted successfully with ID: " + id);
    }
}

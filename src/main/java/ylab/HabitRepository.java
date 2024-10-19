package ylab;

import ylab.entity.habit.Habit;

import java.util.List;

public interface HabitRepository {
    void save(Habit habit);

    List<Habit> findAll();

    void deleteById(Long id);
}

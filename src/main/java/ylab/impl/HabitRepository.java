package ylab.impl;

import ylab.entity.habit.Habit;

import java.util.Map;

public interface HabitRepository {
    /**
     * Save habit in repo
     * @param habit - habit for save in repo
     * @return new Habit() if the save will be successful otherwise null
     */
    Habit save(Habit habit);

    /**
     * Return all habit
     */
    Map<Long, Habit> findAll(long user_id);

    /**
     * Delete habit in repo
     * @param id - id habit
     */
    void deleteById(Long id);
}

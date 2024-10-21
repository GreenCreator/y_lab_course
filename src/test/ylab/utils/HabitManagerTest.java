package ylab.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ylab.entity.habit.Habit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;

public class HabitManagerTest {

    private HabitManager habitManager;
    private Habit habit;

    @BeforeEach
    public void setUp() {
        habitManager = new HabitManager();
        habitManager.createHabit("Exercise", "Do 30 minutes of exercise", "daily");
        habit = habitManager.getHabitByTitle("Exercise");
    }

    @Test
    public void shouldAddHabitSuccessfully() {
        var habits = habitManager.listHabits();
        assertThat(habits).containsKey(habit.getTitle());
    }

    @Test
    public void shouldDeleteHabitSuccessfully() {
        habitManager.deleteHabit("Exercise");
        var habits = habitManager.listHabits();
        assertThat(habits).containsKey(habit.getTitle());
    }
}
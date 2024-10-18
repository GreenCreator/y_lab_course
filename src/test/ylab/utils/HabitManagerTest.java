package ylab.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ylab.entity.habit.Habit;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

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
        List<Habit> habits = habitManager.listHabits();
        assertThat(habits).contains(habit);
    }

    @Test
    public void shouldDeleteHabitSuccessfully() {
        habitManager.deleteHabit("Exercise");
        List<Habit> habits = habitManager.listHabits();
        assertThat(habits).doesNotContain(habit);
    }
}
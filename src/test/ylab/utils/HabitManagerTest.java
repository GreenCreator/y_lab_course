package ylab.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ylab.entity.habit.Habit;
import ylab.entity.user.User;
import ylab.impl.HabitRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class HabitManagerTest {

    private HabitManager habitManager;
    private Habit habit;
    private User user;

    @BeforeEach
    public void setUp() {
        user = Mockito.mock(User.class);
        habitManager = new HabitManager(Mockito.mock(HabitRepositoryImpl.class));
        habitManager.createHabit("Exercise", "Do 30 minutes of exercise", "daily", user.getId());
        habit = habitManager.getHabitByTitle("Exercise");
    }

    @Test
    public void shouldAddHabitSuccessfully() {
        var habits = habitManager.listHabits(user.getId());
        assertThat(habits).containsKey(habit.getId());
    }

    @Test
    public void shouldDeleteHabitSuccessfully() {
        habitManager.deleteHabit("Exercise");
        var habits = habitManager.listHabits(user.getId());
        assertThat(habits).containsKey(habit.getId());
    }
}
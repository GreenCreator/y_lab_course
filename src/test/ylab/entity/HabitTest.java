package ylab.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ylab.entity.habit.Habit;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitTest {
    private Habit habit;

    @BeforeEach
    void setUp() {
        habit = new Habit("Exercise", "Daily workout", "Daily");
    }

    @Test
    void testHabitInitialization() {
        assertEquals("Exercise", habit.getTitle());
        assertEquals("Daily workout", habit.getDescription());
        assertEquals("Daily", habit.getFrequency());
        assertFalse(habit.isCompleted());
        assertEquals(LocalDate.now(), habit.getCreationDate());
        assertTrue(habit.getCompletionHistory().isEmpty());
    }

    @Test
    void testMarkCompleted() {
        habit.markCompleted();
        assertTrue(habit.isCompleted());
        List<LocalDate> completionHistory = habit.getCompletionHistory();
        assertEquals(1, completionHistory.size());
        assertEquals(LocalDate.now(), completionHistory.get(0));
    }

    @Test
    void testCompletionHistory() {
        habit.markCompleted();
        habit.markCompleted();

        List<LocalDate> completionHistory = habit.getCompletionHistory();
        assertEquals(2, completionHistory.size());
        assertEquals(LocalDate.now(), completionHistory.get(0));
        assertEquals(LocalDate.now(), completionHistory.get(1));
    }

    @Test
    void testSetTitle() {
        habit.setTitle("New Title");
        assertEquals("New Title", habit.getTitle());
    }

    @Test
    void testSetDescription() {
        habit.setDescription("New Description");
        assertEquals("New Description", habit.getDescription());
    }

    @Test
    void testSetCompleted() {
        habit.setCompleted(true);
        assertTrue(habit.isCompleted());

        habit.setCompleted(false);
        assertFalse(habit.isCompleted());
    }
}

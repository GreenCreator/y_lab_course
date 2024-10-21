package ylab.utils;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ylab.entity.habit.Habit;
import ylab.entity.user.User;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HabitStatisticsTest {

    @Test
    public void testGetCompletionCountForDay() {
        Habit habit = Mockito.mock(Habit.class);
        LocalDate day = LocalDate.of(2024, 10, 10);
        Mockito.when(habit.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.of(2024, 10, 10),
                LocalDate.of(2024, 10, 11)
        ));

        long count = HabitStatistics.getCompletionCountForDay(habit, day);

        assertEquals(1, count);
    }

    @Test
    public void testGetCompletionCountForWeek() {
        Habit habit = Mockito.mock(Habit.class);
        LocalDate weekStart = LocalDate.of(2024, 10, 6);
        Mockito.when(habit.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.of(2024, 10, 7),
                LocalDate.of(2024, 10, 8),
                LocalDate.of(2024, 10, 9),
                LocalDate.of(2024, 10, 10),
                LocalDate.of(2024, 10, 11)
        ));

        long count = HabitStatistics.getCompletionCountForWeek(habit, weekStart);

        assertEquals(5, count);
    }

    @Test
    public void testGetCompletionCountForMonth() {
        Habit habit = Mockito.mock(Habit.class);
        LocalDate monthStart = LocalDate.of(2024, 10, 1);
        Mockito.when(habit.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 15),
                LocalDate.of(2024, 10, 20)
        ));

        long count = HabitStatistics.getCompletionCountForMonth(habit, monthStart);

        assertEquals(3, count);
    }

    @Test
    public void testGetCurrentStreak() {
        Habit habit = Mockito.mock(Habit.class);
        Mockito.when(habit.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.now(),
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2)
        ));

        int streak = HabitStatistics.getCurrentStreak(habit);

        assertEquals(3, streak); // 3 успешных дня подряд
    }

    @Test
    public void testGetCurrentStreak_NoHabits() {
        Habit habit = Mockito.mock(Habit.class);
        Mockito.when(habit.getCompletionHistory()).thenReturn(Arrays.asList());

        int streak = HabitStatistics.getCurrentStreak(habit);

        assertEquals(0, streak);
    }

    @Test
    public void testGetSuccessPercentage() {
        Habit habit = Mockito.mock(Habit.class);
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        Mockito.when(habit.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 15)
        ));

        double percentage = HabitStatistics.getSuccessPercentage(habit, startDate, endDate);

        assertEquals(6.45, percentage, 0.01); // 2 успешных дня из 31
    }

    @Test
    public void testGetSuccessPercentage_StartDateAfterEndDate() {
        Habit habit = Mockito.mock(Habit.class);
        LocalDate startDate = LocalDate.of(2024, 10, 10);
        LocalDate endDate = LocalDate.of(2024, 10, 1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            HabitStatistics.getSuccessPercentage(habit, startDate, endDate);
        });

        assertEquals("Start date cannot be after end date.", exception.getMessage());
    }

    @Test
    public void testGenerateProgressReport() {
        User user = Mockito.mock(User.class);
        HabitStatistics habitStatistics = Mockito.mock(HabitStatistics.class);
        Habit habit1 = Mockito.mock(Habit.class);
        Habit habit2 = Mockito.mock(Habit.class);

        Mockito.when(habit1.getTitle()).thenReturn("Habit 1");
        Mockito.when(habit2.getTitle()).thenReturn("Habit 2");

        Mockito.when(habit1.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 2)
        ));
        Mockito.when(habit2.getCompletionHistory()).thenReturn(Arrays.asList(
                LocalDate.of(2024, 10, 1)
        ));

        Map<String, Habit> habitsMap = new HashMap<>();
        habitsMap.put(habit1.getTitle(), habit1);
        habitsMap.put(habit2.getTitle(), habit2);

        Mockito.when(user.getHabitManager()).thenReturn(Mockito.mock(HabitManager.class));
        Mockito.when(user.getHabitManager().listHabits()).thenReturn(habitsMap);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        habitStatistics.generateProgressReport(user, startDate, endDate);

        String output = outputStream.toString();
        assertTrue(output.contains("Progress Report from 2024-10-01 to 2024-10-31"));
        assertTrue(output.contains("Habit: Habit 1"));
        assertTrue(output.contains("Habit: Habit 2"));
    }
}

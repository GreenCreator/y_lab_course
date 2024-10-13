package ylab.menu.habit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ylab.entity.habit.Habit;
import ylab.menu.MenuManager;
import ylab.utils.HabitStatistics;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HabitReportMenuTest {

    private Habit habit;
    private MenuManager menuManager;
    private Scanner scanner;
    private HabitReportMenu habitReportMenu;

    @BeforeEach
    void setUp() {
        habit = mock(Habit.class);
        menuManager = mock(MenuManager.class);
        habitReportMenu = new HabitReportMenu(habit, menuManager, new Scanner(System.in));
    }

    @Test
    void testHandleInput_Day() {
        LocalDate today = LocalDate.now();
        List<LocalDate> completionHistory = Arrays.asList(today, today.minusDays(1), today);
        when(habit.getCompletionHistory()).thenReturn(completionHistory);

        habitReportMenu.handleInput(1);

        long expectedCount = HabitStatistics.getCompletionCountForDay(habit, today);
        assertEquals(2, expectedCount);
    }

    @Test
    void testHandleInput_Week() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);

        List<LocalDate> completionHistory = Arrays.asList(
                today, today.minusDays(1), today.minusDays(3), today.minusDays(6),
                weekStart.minusDays(1), weekStart.minusDays(2)
        );
        when(habit.getCompletionHistory()).thenReturn(completionHistory);

        habitReportMenu.handleInput(2);

        long expectedCount = HabitStatistics.getCompletionCountForWeek(habit, weekStart);
        assertEquals(4, expectedCount);
    }


    @Test
    void testHandleInput_Month() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);

        List<LocalDate> completionHistory = Arrays.asList(
                today,
                today.minusDays(1),
                today.minusDays(10),
                today.minusDays(15),
                today.minusMonths(1),
                today.minusMonths(2) // Не должен учитываться
        );

        when(habit.getCompletionHistory()).thenReturn(completionHistory);

        habitReportMenu.handleInput(3);

        long expectedCount = HabitStatistics.getCompletionCountForMonth(habit, monthStart);
        assertEquals(3, expectedCount);
    }

    @Test
    void testHandleInput_CurrentStreak() {
        LocalDate today = LocalDate.now();

        List<LocalDate> completionHistory = Arrays.asList(
                today,
                today.minusDays(1),
                today.minusDays(3), // (перерыв)
                today.minusDays(4),
                today.minusDays(5)
        );

        when(habit.getCompletionHistory()).thenReturn(completionHistory);

        habitReportMenu.handleInput(4);

        int expectedStreak = HabitStatistics.getCurrentStreak(habit);
        assertEquals(expectedStreak, 2);
    }


    @Test
    void testHandleInput_SuccessPercentage() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);
        LocalDate endDate = today;

        List<LocalDate> completionHistory = Arrays.asList(
                today,
                today.minusDays(1),
                today.minusDays(3),
                today.minusDays(4)
        );

        when(habit.getCompletionHistory()).thenReturn(completionHistory);

        String input = startDate.toString() + "\n" + endDate.toString() + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        habitReportMenu = new HabitReportMenu(habit, menuManager, scanner);
        habitReportMenu.handleInput(5);

        double expectedSuccessPercentage = HabitStatistics.getSuccessPercentage(habit, startDate, endDate);
        assertEquals(expectedSuccessPercentage, 57.14, 0.01);
    }

    @Test
    void testHandleInput_Back() {
        habitReportMenu.handleInput(6);
        verify(menuManager).popMenu();
    }

    @Test
    void testHandleInput_InvalidChoice() {
        habitReportMenu.handleInput(999);
        verifyNoInteractions(habit);
    }
}

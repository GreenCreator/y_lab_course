package ylab.menu.habit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ylab.entity.habit.Habit;
import ylab.entity.user.User;
import ylab.menu.MenuManager;
import ylab.utils.HabitManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class HabitTrackingMenuTest {

    private User user;
    private MenuManager menuManager;
    private Scanner scanner;
    private HabitTrackingMenu habitTrackingMenu;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        menuManager = mock(MenuManager.class);
        scanner = new Scanner(System.in);
        habitTrackingMenu = new HabitTrackingMenu(user, menuManager, scanner);
    }

    @Test
    void testHandleInput_MarkHabitComplete() {
        String habitName = "Test Habit";
        Habit habit = mock(Habit.class);

        when(user.getHabitManager()).thenReturn(mock(HabitManager.class));
        when(user.getHabitManager().getHabitByTitle(habitName)).thenReturn(habit);

        String input = habitName + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        habitTrackingMenu = new HabitTrackingMenu(user, menuManager, scanner);

        habitTrackingMenu.handleInput(1);

        verify(habit).markCompleted();
    }

    @Test
    void testHandleInput_GenerateHabitReport() {
        String habitTitle = "Test Habit";
        Habit habit = mock(Habit.class);

        when(user.getHabitManager()).thenReturn(mock(HabitManager.class));
        when(user.getHabitManager().getHabitByTitle(habitTitle)).thenReturn(habit);

        String input = habitTitle + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        habitTrackingMenu = new HabitTrackingMenu(user, menuManager, scanner);

        habitTrackingMenu.handleInput(2);

        verify(menuManager).pushMenu(any());
    }

    @Test
    void testHandleInput_GenerateProgressReport() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 7);

        when(user.getHabitManager()).thenReturn(mock(HabitManager.class));

        String input = startDate + "\n" + endDate + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        habitTrackingMenu = new HabitTrackingMenu(user, menuManager, scanner);

        habitTrackingMenu.handleInput(3);

        verify(user, times(1)).getHabitManager();
    }

    @Test
    void testHandleInput_InvalidChoice() {
        habitTrackingMenu.handleInput(99);
    }
}

package ylab.menu.habit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ylab.entity.habit.Habit;
import ylab.entity.user.User;
import ylab.menu.MenuManager;
import ylab.utils.HabitManager;
import ylab.utils.UserManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HabitMenuTest {

    private User user;
    private MenuManager menuManager;
    private UserManager userManager;
    private HabitManager habitManager;
    private HabitMenu habitMenu;
    private Scanner scanner;

    @BeforeEach
    public void setup() {
        user = mock(User.class);
        menuManager = Mockito.mock(MenuManager.class);
        userManager = Mockito.mock(UserManager.class);
        habitManager = mock(HabitManager.class);
        habitMenu = new HabitMenu(user, menuManager, scanner);
    }

    @Test
    public void testHandleInput_CreateHabit() {

        String input = "New Habit\nNew Description\ndaily\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);


        when(user.getHabitManager()).thenReturn(habitManager);
        habitMenu = new HabitMenu(user, menuManager, scanner);

        habitMenu.handleInput(1);

        verify(user.getHabitManager()).createHabit("New Habit", "New Description", "daily");
    }

    @Test
    public void testHandleInput_EditHabit() {

        String input = "Existing Habit\nSome Description\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        Habit habit = mock(Habit.class);

        when(user.getHabitManager()).thenReturn(habitManager);
        when(habitManager.getHabitByTitle("Existing Habit")).thenReturn(habit);

        habitMenu = new HabitMenu(user, menuManager, scanner);

        habitMenu.handleInput(2);

        verify(habit).setDescription("Some Description");
    }

    @Test
    public void testHandleInput_EditHabit_HabitNotFound() {
        String input = "Nonexistent Habit\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        when(user.getHabitManager()).thenReturn(habitManager);
        when(habitManager.getHabitByTitle(input)).thenReturn(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        habitMenu = new HabitMenu(user, menuManager, scanner);
        habitMenu.handleInput(2);

        assertEquals("Enter habit title: Not found habit\n", outputStream.toString());
    }

    @Test
    public void testHandleInput_DeleteHabit() {
        String input = "Habit to Delete\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        when(user.getHabitManager()).thenReturn(habitManager);
        when(habitManager.getHabitByTitle(input)).thenReturn(null);

        habitMenu = new HabitMenu(user, menuManager, scanner);
        habitMenu.handleInput(3);

        verify(habitManager).deleteHabit("Habit to Delete");
    }

    @Test
    public void testHandleInput_ViewHabits() {
        habitMenu.handleInput(4);

        verify(menuManager).pushMenu(any(HabitViewMenu.class));
    }
}
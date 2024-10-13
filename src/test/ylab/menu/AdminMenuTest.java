package ylab.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ylab.entity.admin.Admin;
import ylab.menu.admin.AdminMenu;
import ylab.utils.UserManager;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class AdminMenuTest {

    private AdminMenu adminMenu;
    private Admin admin;
    private UserManager userManager;
    private MenuManager menuManager;
    private Scanner scanner;

    @BeforeEach
    public void setup() {
        userManager = Mockito.mock(UserManager.class);
        menuManager = Mockito.mock(MenuManager.class);
        admin = mock(Admin.class);

        adminMenu = new AdminMenu(admin, userManager, menuManager, scanner);
    }

    @Test
    public void testDisplay() {
        adminMenu.display();
    }

    @Test
    public void testHandleInput_ViewAllUsers() {
        adminMenu.handleInput(1);
        verify(admin).listAllUsers();
    }

    @Test
    public void testHandleInput_BlockUser() {
        String input = "test@example.com\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        adminMenu = new AdminMenu(admin, userManager, menuManager, scanner);

        adminMenu.handleInput(2);
        verify(userManager).blockUser("test@example.com");
    }

    @Test
    public void testHandleInput_DeleteUser_NotAdmin() {
        String input = "test@example.com\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);
        adminMenu = new AdminMenu(admin, userManager, menuManager, scanner);

        adminMenu.handleInput(3);
        verify(admin).deleteUser("test@example.com");
        verify(menuManager, never()).popToMainMenu();
    }

    @Test
    public void testHandleInput_DeleteUser_IsAdmin() {
        String input = "admin@example.com\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        when(admin.getEmail()).thenReturn("admin@example.com");

        adminMenu = new AdminMenu(admin, userManager, menuManager, scanner);

        adminMenu.handleInput(3);

        verify(admin).deleteUser("admin@example.com");
        verify(menuManager).popToMainMenu();
    }

    @Test
    public void testHandleInput_Logout() {
        adminMenu.handleInput(4);
        verify(menuManager).popMenu();
    }

    @Test
    public void testHandleInput_InvalidChoice() {
        adminMenu.handleInput(0);
        verifyNoInteractions(admin, userManager, menuManager);
    }
}
package ylab.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuManagerTest {

    private MenuManager menuManager;
    private IMenu mainMenu;
    private IMenu subMenu;

    @BeforeEach
    void setUp() {
        menuManager = new MenuManager();
        mainMenu = Mockito.mock(IMenu.class);
        subMenu = Mockito.mock(IMenu.class);
    }

    @Test
    void testPushMenu() {
        menuManager.pushMenu(mainMenu);
        assertEquals(1, menuManager.getMenuStackSize(), "Stack size should be 1 after pushing a menu.");
    }

    @Test
    void testPopMenu() {
        menuManager.pushMenu(mainMenu);
        menuManager.popMenu();
        assertEquals(0, menuManager.getMenuStackSize(), "Stack should be empty after popping the menu.");
    }

    @Test
    void testPopToMainMenu() {
        menuManager.pushMenu(mainMenu);
        menuManager.pushMenu(subMenu);
        menuManager.popToMainMenu();
        assertEquals(1, menuManager.getMenuStackSize(), "Stack size should be 1 after popping to main menu.");
    }

    @Test
    void testExit() {
        menuManager.exit();
        assertEquals(false, menuManager.isRunning(), "Menu manager should not be running after calling exit.");
    }
}

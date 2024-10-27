package ylab;

import ylab.impl.HabitRepositoryImpl;
import ylab.impl.UserRepositoryImpl;
import ylab.menu.MainMenu;
import ylab.menu.MenuManager;
import ylab.utils.UserManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ConnectRepo connectRepo = new ConnectRepo();

        var connection = connectRepo.getConnection();

        var habitRepository = new HabitRepositoryImpl(connection);
        UserManager userManager = new UserManager(new UserRepositoryImpl(connection, habitRepository), habitRepository);
        Scanner scanner = new Scanner(System.in);
        MenuManager menuManager = new MenuManager();

        menuManager.pushMenu(new MainMenu(userManager, menuManager, scanner, connectRepo));
        menuManager.start();

    }
}


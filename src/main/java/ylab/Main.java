package ylab;

import io.aspect.AspectHandler;
import io.dto.UserDTO;
import io.service.UserService;
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
        var userRepositoryImpl = new UserRepositoryImpl(connection, habitRepository);
        UserManager userManager = new UserManager(userRepositoryImpl, habitRepository);

        UserService userService = new UserService(userRepositoryImpl);
        UserService userServiceProxy = AspectHandler.createProxy(userService);

//        userServiceProxy.createUser(new UserDTO());
//        userServiceProxy.getUserById(1L);

        Scanner scanner = new Scanner(System.in);
        MenuManager menuManager = new MenuManager();

        menuManager.pushMenu(new MainMenu(userManager, menuManager, scanner, connectRepo));
        menuManager.start();

    }
}


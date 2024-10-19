package ylab;

import ylab.menu.MainMenu;
import ylab.menu.MenuManager;
import ylab.utils.UserManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            var result = statement.executeQuery("SELECT count(*) FROM information_schema.tables");
            while (result.next()) {
                System.out.println("Table count: " + result.getInt("count"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        UserManager userManager = new UserManager(new HashMap<>());
        Scanner scanner = new Scanner(System.in);
        MenuManager menuManager = new MenuManager();

        menuManager.pushMenu(new MainMenu(userManager, menuManager, scanner));
        menuManager.start();
    }
}


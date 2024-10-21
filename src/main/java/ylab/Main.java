package ylab;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ylab.menu.MainMenu;
import ylab.menu.MenuManager;
import ylab.utils.UserManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            System.out.println("Migration is complete successfully.");
        } catch (SQLException | LiquibaseException e) {
            System.out.println(e.getMessage());
        }


        UserManager userManager = new UserManager(new HashMap<>());
        Scanner scanner = new Scanner(System.in);
        MenuManager menuManager = new MenuManager();

        menuManager.pushMenu(new MainMenu(userManager, menuManager, scanner));
        menuManager.start();
    }
}


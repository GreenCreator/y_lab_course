package ylab;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ylab.impl.HabitRepository;
import ylab.impl.HabitRepositoryImpl;
import ylab.impl.UserRepositoryImpl;
import ylab.menu.MainMenu;
import ylab.menu.MenuManager;
import ylab.utils.UserManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Config config = new Config();
        String dbUrl = config.getDbUrl();
        String dbUsername = config.getDbUsername();
        String dbPassword = config.getDbPassword();
        String changeLogFile = config.getLiquibaseChangeLogFile();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            System.out.println("Migration is complete successfully.");

            var habitRepository = new HabitRepositoryImpl(connection);
            UserManager userManager = new UserManager(new UserRepositoryImpl(connection, habitRepository), habitRepository);
            Scanner scanner = new Scanner(System.in);
            MenuManager menuManager = new MenuManager();

            menuManager.pushMenu(new MainMenu(userManager, menuManager, scanner));
            menuManager.start();
        } catch (SQLException | LiquibaseException e) {
            System.out.println(e.getMessage());
        }
    }
}


package ylab;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectRepo {

    private Connection connection;

    public ConnectRepo() {
        initializeConnection();
    }

    private void initializeConnection() {
        Config config = new Config();
        String dbUrl = config.getDbUrl();
        String dbUsername = config.getDbUsername();
        String dbPassword = config.getDbPassword();
        String changeLogFile = config.getLiquibaseChangeLogFile();

        try {
            this.connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            System.out.println("Migration is complete successfully.");
        } catch (SQLException | LiquibaseException e) {
            System.out.println("Connection or migration failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

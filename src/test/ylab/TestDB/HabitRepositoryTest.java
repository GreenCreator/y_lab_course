package ylab.TestDB;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ylab.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HabitRepositoryTest {

    private PostgreSQLContainer<?> postgreSQLContainer;

    @BeforeEach
    public void setUp() {
        Config config = new Config();
        postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("postgres")
                .withUsername(config.getDbUsername())
                .withPassword(config.getDbPassword());
        postgreSQLContainer.start();
    }

    @AfterEach
    public void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    public void testDatabaseMigration() {
        String dbUrl = postgreSQLContainer.getJdbcUrl();
        String dbUsername = postgreSQLContainer.getUsername();
        String dbPassword = postgreSQLContainer.getPassword();
        String changeLogFile = "db/changelog/db.changelog-master.xml";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            System.out.println("Migration is complete successfully.");
        } catch (SQLException | liquibase.exception.LiquibaseException e) {
            System.out.println(e.getMessage());
        }
    }
}


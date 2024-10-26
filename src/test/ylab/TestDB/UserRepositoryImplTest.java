package ylab.TestDB;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ylab.Config;
import ylab.entity.user.User;
import ylab.impl.HabitRepositoryImpl;
import ylab.impl.UserRepositoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class UserRepositoryImplTest {
    static Config config = new Config();

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("postgres-db")
            .withUsername(config.getDbUsername())
            .withPassword(config.getDbPassword());

    private UserRepositoryImpl userRepository;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException, LiquibaseException {
        postgresContainer.start();
        connection = postgresContainer.createConnection("");

        // Инициализация Liquibase
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("classpath:/db/changelog/00-create-schema.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update(new Contexts());

        connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );
        userRepository = new UserRepositoryImpl(connection, new HabitRepositoryImpl(connection));
        connection.setAutoCommit(false);
        createSchema(); // Создаем необходимые таблицы
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close(); // Закрываем соединение после каждого теста
        }
    }

    private void createSchema() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS entity.users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255) UNIQUE, " +
                    "password VARCHAR(255), " +
                    "blocked_status BOOLEAN, " +
                    "admin BOOLEAN" +
                    ")");
        }
    }

    @Test
    public void testSaveUser() throws SQLException {
        User user = new User("Test User", "test@example.com", "password", false, new HabitRepositoryImpl(connection));
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("test@example.com");
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("Test User", foundUser.getName());
    }

    @Test
    public void testFindAllUsers() throws SQLException {
        userRepository.save(new User("User 1", "user1@example.com", "password", false, new HabitRepositoryImpl(connection)));
        userRepository.save(new User("User 2", "user2@example.com", "password", false, new HabitRepositoryImpl(connection)));

        List<User> users = userRepository.findAll();
        Assertions.assertEquals(3, users.size());
    }

    @Test
    public void testDeleteUserByEmail() throws SQLException {
        userRepository.save(new User("User to delete", "delete@example.com", "password", false, new HabitRepositoryImpl(connection)));

        userRepository.deleteByEmail("delete@example.com");
        User deletedUser = userRepository.findByEmail("delete@example.com");

        Assertions.assertNull(deletedUser);
    }

    @Test
    public void testUpdateUserName() throws SQLException {
        User user = new User("Old Name", "update@example.com", "password", false, new HabitRepositoryImpl(connection));
        userRepository.save(user);
        long userId = user.getId();

        userRepository.updateNameUser("New Name", userId);
        User updatedUser = userRepository.findByEmail("update@example.com");

        Assertions.assertEquals("New Name", updatedUser.getName());
    }
}

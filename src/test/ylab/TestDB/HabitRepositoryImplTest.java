package ylab.TestDB;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ylab.Config;
import ylab.entity.habit.Habit;
import ylab.impl.HabitRepositoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class HabitRepositoryImplTest {

    static Config config = new Config();
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("postgres-db")
            .withUsername(config.getDbUsername())
            .withPassword(config.getDbPassword());

    private Connection connection;
    private HabitRepositoryImpl habitRepository;

    @BeforeAll
    public static void init() {
        postgresContainer.start();
    }

    @BeforeEach
    public void setup() throws SQLException, LiquibaseException {

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
        habitRepository = new HabitRepositoryImpl(connection);

        // Создание таблицы для тестов
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS entity.habits (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(100),
                    description TEXT,
                    frequency VARCHAR(50),
                    completed BOOLEAN,
                    creation_date DATE,
                    user_id BIGINT
                );
                """;
        connection.createStatement().execute(createTableSQL);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблицы после каждого теста
        connection.createStatement().execute("TRUNCATE TABLE entity.habits RESTART IDENTITY;");
        connection.close();
    }

    @Test
    public void testSave() {
        Habit habit = new Habit(0, "Test Title", "Test Description", "Daily", false, LocalDate.now(), 1L);
        Habit savedHabit = habitRepository.save(habit);

        assertNotNull(savedHabit);
        assertNotNull(savedHabit.getId());
        assertEquals("Test Title", savedHabit.getTitle());
    }

    @Test
    public void testFindAll() {
        Habit habit1 = new Habit(0, "Habit1", "Description1", "Weekly", true, LocalDate.now(), 1L);
        Habit habit2 = new Habit(0, "Habit2", "Description2", "Monthly", false, LocalDate.now(), 1L);

        habitRepository.save(habit1);
        habitRepository.save(habit2);

        Map<Long, Habit> habits = habitRepository.findAll(1L);

        assertEquals(2, habits.size());
        assertTrue(habits.values().stream().anyMatch(habit -> habit.getTitle().equals("Habit1")));
        assertTrue(habits.values().stream().anyMatch(habit -> habit.getTitle().equals("Habit2")));
    }

    @Test
    public void testDeleteById() {
        Habit habit = new Habit(0, "ToBeDeleted", "Some Description", "Daily", false, LocalDate.now(), 1L);
        Habit savedHabit = habitRepository.save(habit);
        assertNotNull(savedHabit);

        habitRepository.deleteById(savedHabit.getId());

        Habit deletedHabit = habitRepository.findByTitle("ToBeDeleted");
        assertNull(deletedHabit);
    }

    @Test
    public void testFindByTitle() {
        Habit habit = new Habit(0, "UniqueTitle", "Test Description", "Daily", false, LocalDate.now(), 1L);
        habitRepository.save(habit);

        Habit foundHabit = habitRepository.findByTitle("UniqueTitle");

        assertNotNull(foundHabit);
        assertEquals("UniqueTitle", foundHabit.getTitle());
    }

    @Test
    public void testUpdateDescriptionByTitle() {
        Habit habit = new Habit(0, "UpdateTitle", "Old Description", "Daily", false, LocalDate.now(), 1L);
        habitRepository.save(habit);

        habitRepository.updateDescriptionByTitle("UpdateTitle", "New Description");

        Habit updatedHabit = habitRepository.findByTitle("UpdateTitle");
        assertNotNull(updatedHabit);
        assertEquals("New Description", updatedHabit.getDescription());
    }
}

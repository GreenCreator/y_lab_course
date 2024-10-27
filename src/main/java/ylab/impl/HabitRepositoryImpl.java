package ylab.impl;

import ylab.entity.habit.Habit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HabitRepositoryImpl implements HabitRepository {

    private static final String SAVE_SQL = "INSERT INTO entity.habits (id, title, description, frequency, completed, creation_date, user_id) " +
            "VALUES (nextval('entity.habit_id_seq'), ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String SELECT_FROM_USER_ID_SQL = "SELECT * FROM entity.habits WHERE user_id = ?";
    private static final String SELECT_SQL = "SELECT * FROM entity.habits WHERE id = ?";
    private static final String SELECT_BY_TITLE = "SELECT * FROM entity.habits WHERE title = ?";
    private static final String UPDATE_DESCRIPTION_SQL = "UPDATE entity.habits SET description = ? WHERE title = ?";


    private final Connection connection;

    public HabitRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Habit save(Habit habit) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setString(1, habit.getTitle());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency());
            statement.setBoolean(4, habit.isCompleted());
            statement.setDate(5, java.sql.Date.valueOf(habit.getCreationDate()));
            statement.setLong(6, habit.getUserId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long generatedId = resultSet.getLong("id");
                    habit.setId(generatedId);
                    System.out.println("Habit saved with id: " + generatedId);
                    return habit;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<Long, Habit> findAll(long user_id) {
        Map<Long, Habit> habits = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_FROM_USER_ID_SQL)) {
            stmt.setLong(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Habit habit = new Habit(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("frequency"),
                        rs.getBoolean("completed"),
                        rs.getDate("creation_date").toLocalDate(),
                        rs.getLong("user_id")
                );
                habits.put(habit.getId(), habit);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return habits;
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM entity.habits WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();
            connection.commit(); // Если у вас включен режим автокоммита, это может быть не нужно.
            if (rowsAffected > 0) {
                System.out.println("Habit with id " + id + " deleted successfully.");
            } else {
                System.out.println("No habit found with id " + id + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting habit: " + e.getMessage());
        }
    }


    public Habit findByTitle(String title) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_TITLE)) {
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Habit(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getString("frequency"),
                            resultSet.getBoolean("completed"),
                            resultSet.getDate("creation_date").toLocalDate(),
                            resultSet.getLong("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateDescriptionByTitle(String title, String newDescription) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_DESCRIPTION_SQL)) {
            statement.setString(1, newDescription);
            statement.setString(2, title);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Habit description updated in the database.");
            } else {
                System.out.println("No habit found with the provided title.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

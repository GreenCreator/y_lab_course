package ylab.impl;

import ylab.entity.habit.Habit;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class HabitRepositoryImpl implements HabitRepository {

    private static final String SAVE_Sql = "INSERT INTO your_schema.habits (title, description, frequency, creation_date) VALUES (?, ?, ?, ?)";

    private final Connection connection;

    public HabitRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Habit save(Habit habit) {
        String sql = "INSERT INTO entity.habits (id, title, description, frequency, completed, creation_date, user_id) " +
                "VALUES (nextval('entity.habit_id_seq'), ?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
        String sql = "SELECT * FROM entity.habits WHERE user_id = ?"; // Используем параметр вместо прямого сравнения
        Map<Long, Habit> habits = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) { // Используем PreparedStatement для параметризированного запроса
            stmt.setLong(1, user_id); // Устанавливаем значение user_id
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
        String sql = "DELETE FROM entity.habits WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Habit findByTitle(String title) {
        String sql = "SELECT * FROM entity.habits WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
        String sql = "UPDATE entity.habits SET description = ? WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

package ylab;

import ylab.entity.habit.Habit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitRepositoryImpl implements HabitRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    @Override
    public void save(Habit habit) {
        String sql = "INSERT INTO your_schema.habits (title, description, frequency, creation_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, habit.getTitle());
            pstmt.setString(2, habit.getDescription());
            pstmt.setString(3, habit.getFrequency());
            pstmt.setDate(4, Date.valueOf(habit.getCreationDate()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Habit> findAll() {
        String sql = "SELECT * FROM your_schema.habits";
        List<Habit> habits = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Habit habit = new Habit(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("frequency")
                );
                habit.setCreationDate(rs.getDate("creation_date").toLocalDate());
                habits.add(habit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return habits;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM your_schema.habits WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

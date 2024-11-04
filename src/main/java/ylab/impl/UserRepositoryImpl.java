package ylab.impl;

import ylab.entity.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;
    private final HabitRepositoryImpl habitRepository;

    public UserRepositoryImpl(Connection connection, HabitRepositoryImpl habitRepository) {
        this.connection = connection;
        this.habitRepository = habitRepository;
    }

    @Override
    public void save(User user) throws SQLException {
        String query = "INSERT INTO entity.users (id, name, email, password, blocked_status, admin) " +
                "VALUES (nextval('entity.user_id_seq'), ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setBoolean(4, user.GetBlockedStatus());
            statement.setBoolean(5, user.isAdmin());

            try (ResultSet generatedKeys = statement.executeQuery()) {
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong("id");
                    user.setId(generatedId);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public User findById(long id) throws SQLException {
        String query = "SELECT * FROM entity.users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("admin"),
                        habitRepository
                );
                user.setId(resultSet.getLong("id"));
                user.setBlockedStatus(resultSet.getBoolean("blocked_status"));
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM entity.users";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("admin"),
                        habitRepository
                );
                user.setId(resultSet.getLong("id"));
                user.setBlockedStatus(resultSet.getBoolean("blocked_status"));
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public void deleteById(long id) throws SQLException {
        String query = "DELETE FROM entity.users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        }
    }

    @Override
    public void updateNameUser(String name, long id) {
        String sql = "UPDATE entity.users SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setLong(2, id);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User name updated in the database.");
            } else {
                System.out.println("No user found with the provided id.");
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmailUser(String email, long id) {
        String sql = "UPDATE entity.users SET email = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setLong(2, id);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User email updated in the database.");
            } else {
                System.out.println("No user found with the provided id.");
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updatePasswordUser(String password, long id) {
        String sql = "UPDATE entity.users SET password = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password);
            statement.setLong(2, id);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User password updated in the database.");
            } else {
                System.out.println("No user found with the provided id.");
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

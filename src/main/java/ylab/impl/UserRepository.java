package ylab.impl;

import ylab.entity.user.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    /**
     * Save user in repo
     */
    void save(User user) throws SQLException;

    /**
     * Find user by Email
     */
    User findById(long id) throws SQLException;

    /**
     * Get all user from repo
     */
    List<User> findAll() throws SQLException;

    /**
     * Delete user in repo
     */
    void deleteById(long id) throws SQLException;

    /**
     * Update name on user
     */
    void updateNameUser(String name, long id);

    /**
     * Update email on user
     */
    void updateEmailUser(String email, long id);

    /**
     * Update password on user
     */
    void updatePasswordUser(String password, long id);
}

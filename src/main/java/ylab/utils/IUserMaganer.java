package ylab.utils;

import ylab.entity.user.User;

import java.util.List;

public interface IUserMaganer {

    /**
     * Create user
     */
    boolean registerUser(String name, String email, String password);

    /**
     * Create admin
     */
    boolean registerAdmin(String name, String email, String password);

    /**
     * Login user or admin
     */
    User login(String email, String password);

    /**
     * Delete user or admin
     */
    void deleteUser(String email);

    /**
     * Find user by email
     */
    User getUserByEmail(String email);

    /**
     * Get list user
     */
    List<User> getAllUsers();

    /**
     * Blocked user
     */
    void blockUser(String email);
}

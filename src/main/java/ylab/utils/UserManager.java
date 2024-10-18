package ylab.utils;

import ylab.entity.admin.Admin;
import ylab.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManager implements IUser {

    private Map<String, User> users;

    public UserManager(Map<String, User> users) {
        this.users = users;
    }

    public boolean registerUser(String name, String email, String password) {
        if (users.containsKey(email)) {
            System.out.println("User with this email already exists.");
            return false;
        }
        users.put(email, new User(name, email, password, false));
        System.out.println("Registration successful.");
        return true;
    }

    public boolean registerAdmin(String name, String email, String password, UserManager userManager) {
        if (users.containsKey(email)) {
            System.out.println("User with this email already exists.");
            return false;
        }
        users.put(email, new Admin(name, email, password, userManager));
        System.out.println("Registration successful.");
        return true;
    }

    public User login(String email, String password) {
        if (!users.containsKey(email)) {
            System.out.println("User not found.");
            return null;
        }

        User user = users.get(email);
        if (user.getPassword().equals(password)) {
            System.out.println("Login successful.");
            return user;
        }

        System.out.println("Invalid password.");
        return null;
    }

    public void deleteUser(String email) {
        if (users.remove(email) != null) {
            System.out.println("User deleted.");
        } else {
            System.out.println("User not found.");
        }
    }

    public User getUserByEmail(String email) {
        return users.get(email);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Метод для блокировки пользователя
    public void blockUser(String email) {
        var user = getUserByEmail(email);
        user.setBlockedStatus(true);
    }
}


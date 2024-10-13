package ylab.utils;

import ylab.entity.admin.Admin;
import ylab.entity.user.User;

import java.util.*;

public class UserManager {
    private Map<String, User> users = new HashMap<>();

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
        if (users.containsKey(email)) {
            User user = users.get(email);
            if (user.getPassword().equals(password)) {
                if (user.GetBlockedStatus()) {
                    System.out.println("Login successful.");
                } else {
                    System.out.println("Login successful.");
                }
                return user;
            } else {
                System.out.println("Invalid password.");
            }
        } else {
            System.out.println("User not found.");
        }
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
        Optional<User> user = findUserByEmail(email);
        user.ifPresent(u -> u.setBlockedStatus(true)); // Устанавливаем флаг блокировки
    }

    // Метод для поиска пользователя по email
    private Optional<User> findUserByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
}


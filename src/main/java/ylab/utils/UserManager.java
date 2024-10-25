package ylab.utils;

import ylab.entity.admin.Admin;
import ylab.entity.user.User;
import ylab.impl.HabitRepositoryImpl;
import ylab.impl.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class UserManager implements IUserMaganer {

    private final UserRepositoryImpl userRepository;
    private final HabitRepositoryImpl habitRepository;

    public UserManager(UserRepositoryImpl userRepository, HabitRepositoryImpl habitRepository) {
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
    }

    public boolean registerUser(String name, String email, String password) {
        try {
            if (userRepository.findByEmail(email) != null) {
                System.out.println("User with this email already exists.");
                return false;
            }
            User user = new User(name, email, password, false, habitRepository);
            userRepository.save(user);
            System.out.println("Registration successful.");
            return true;
        } catch (SQLException e) {
            System.out.println("Registration failed.");
            return false;
        }
    }

    public boolean registerAdmin(String name, String email, String password) {
        try {
            if (userRepository.findByEmail(email) != null) {
                System.out.println("User with this email already exists.");
                return false;
            }
            Admin admin = new Admin(name, email, password, this);
            userRepository.save(admin);
            System.out.println("Registration successful.");
            return true;
        } catch (SQLException e) {
            System.out.println("Registration failed.");
            return false;
        }
    }

    public User login(String email, String password) {
        try {
            var user = userRepository.findByEmail(email);
            if (user == null) {
                System.out.println("User not found.");
                return null;
            }
            if (user.getPassword().equals(password)) {
                System.out.println("Login successful.");
                return user;
            } else {
                System.out.println("Invalid password.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteUser(String email) {
        try {
            userRepository.deleteByEmail(email);
            System.out.println("User deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }


    public void blockUser(String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                user.setBlockedStatus(true);
                userRepository.save(user);
                System.out.println("User blocked.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateNameUser(User user) {
        userRepository.updateNameUser(user.getName(), user.getId());
    }

    public void updateEmailUser(User user) {
        userRepository.updateEmailUser(user.getEmail(), user.getId());
    }

    public void updatePasswordUser(User user) {
        userRepository.updatePasswordUser(user.getPassword(), user.getId());
    }

    public HabitRepositoryImpl getHabitRepository() {
        return habitRepository;
    }
}


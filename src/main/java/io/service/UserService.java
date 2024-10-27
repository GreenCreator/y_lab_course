package io.service;

import io.annotations.AuditAction;
import io.annotations.Loggable;
import io.dto.UserDTO;
import ylab.entity.user.User;
import ylab.impl.UserRepositoryImpl;

import java.sql.SQLException;

public class UserService {

    private final UserRepositoryImpl userRepository;

    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @AuditAction("User created")
    @Loggable
    public void createUser(UserDTO userDTO) {
        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getAdmin(), null);

        try {
            userRepository.save(user);
            System.out.println("User created successfully: " + user);
        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
        }
    }

    @Loggable
    public UserDTO getUserByEmail(String email) {
        User user = null;
        try {
            user = userRepository.findByEmail(email); // Получение пользователя по ID
        } catch (SQLException e) {
            System.err.println("Error while fetching user: " + e.getMessage());
        }

        if (user != null) {
            return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.isAdmin(), user.GetBlockedStatus());
        }

        return null;
    }
}
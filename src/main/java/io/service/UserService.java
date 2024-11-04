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
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getAdmin(), null);

        try {
            userRepository.save(user);
            System.out.println("User created successfully: " + user);
            var findUser = userRepository.findByEmail(userDTO.getEmail());
            return new UserDTO(findUser.getId(), findUser.getName(), findUser.getEmail(), findUser.getPassword(), findUser.isAdmin(), findUser.GetBlockedStatus());
        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
        }
        return null;
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

    @Loggable
    public boolean deleteByEmail(String email) {
        try {
            userRepository.deleteByEmail(email);
            return true;
        } catch (SQLException e) {
            System.out.println("Error while delete user: " + e.getMessage());
        }
        return false;
    }


    @Loggable
    public UserDTO updateUser(UserDTO userDTO) {
        try {
            userRepository.updateNameUser(userDTO.getName(), userDTO.getId());
            userRepository.updateEmailUser(userDTO.getEmail(), userDTO.getId());
            userRepository.updatePasswordUser(userDTO.getPassword(), userDTO.getId());

            var user = userRepository.findByEmail(userDTO.getEmail());
            return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.isAdmin(), user.GetBlockedStatus());
        } catch (SQLException e) {
            System.out.println("Error while updating user: " + e.getMessage());
        }
        return null;
    }
}
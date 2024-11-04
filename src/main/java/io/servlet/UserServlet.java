package io.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dto.UserDTO;
import io.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Чтение JSON-запроса и преобразование в UserDTO
            UserDTO userDTO = objectMapper.readValue(request.getReader(), UserDTO.class);

            // Создание пользователя через сервис
            var createdUser = userService.createUser(userDTO);

            // Установка статус-кода и возврат JSON-ответа
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), createdUser);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error creating user: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email != null) {
            try {
                Optional<UserDTO> userDTOOptional = Optional.ofNullable(userService.getUserByEmail(email));

                if (userDTOOptional.isPresent()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    objectMapper.writeValue(response.getWriter(), userDTOOptional.get());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("User not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid user ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("User ID is required");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDTO userDTO = objectMapper.readValue(request.getReader(), UserDTO.class);

            // Обновление пользователя через сервис
            var updatedUser = userService.updateUser(userDTO);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), updatedUser);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error updating user: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email != null) {
            try {
                // Удаление пользователя по ID через сервис
                boolean deleted = userService.deleteByEmail(email);

                if (deleted) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("User not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid user ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("User ID is required");
        }
    }
}

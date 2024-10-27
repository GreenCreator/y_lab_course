package io.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dto.HabitDTO;
import io.service.HabitService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;

@WebServlet("/habits")
public class HabitController extends HttpServlet {
    private final HabitService habitService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
        this.objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HabitDTO habitDTO = objectMapper.readValue(request.getInputStream(), HabitDTO.class);
        Set<ConstraintViolation<HabitDTO>> violations = validator.validate(habitDTO);
        if (!violations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Validation failed: " + violations.iterator().next().getMessage());
            return;
        }

        habitService.createHabit(habitDTO);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String idParam = request.getParameter("user_id");
        if (title != null) {
            HabitDTO habitDTO = habitService.getHabitByTitle(title);
            if (habitDTO != null) {
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(habitDTO));
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            Long userId = Long.valueOf(idParam);
            var habits = habitService.getAllHabits(userId);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(habits));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            Long id = Long.valueOf(idParam);
            habitService.deleteHabitById(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}


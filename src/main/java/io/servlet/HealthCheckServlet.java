package io.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.annotations.Loggable;
import io.domain.HealthResponseDto;
import io.service.HealthCheckService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Loggable
@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {

    private final ObjectMapper mapper;

    private final HealthCheckService healthCheckService;

    public HealthCheckServlet() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        healthCheckService = new HealthCheckService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        var healthStatus = healthCheckService.getHealthStatus();
        var healthResponseDto = new HealthResponseDto(healthStatus);
        var bytes = mapper.writeValueAsBytes(healthResponseDto);
        resp.getOutputStream().write(bytes);
    }
}

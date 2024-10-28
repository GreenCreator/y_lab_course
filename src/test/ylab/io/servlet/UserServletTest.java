package ylab.io.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dto.UserDTO;
import io.service.UserService;
import io.servlet.UserServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServletTest {

    private UserServlet userServlet;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userServlet = new UserServlet(userService);
    }

    @Test
    public void testDoPost_CreateUser_ReturnsCreatedUser() throws Exception {
        // Подготовка данных
        var userDTO = new UserDTO(1L, "Ivan", "test@example.com", "password", false, false);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Вызов метода doPost
        userServlet.doPost(request, response);

        // Проверка результата
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        printWriter.flush();

        // Проверка JSON-ответа
        String jsonResponse = stringWriter.toString();
        UserDTO result = objectMapper.readValue(jsonResponse, UserDTO.class);

        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }
}

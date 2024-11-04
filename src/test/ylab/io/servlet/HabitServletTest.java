package ylab.io.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dto.HabitDTO;
import io.service.HabitService;
import io.servlet.HabitServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class HabitServletTest {
    @Mock
    private HabitService habitService;

    @InjectMocks
    private HabitServlet habitController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoPost_ValidRequest() throws Exception {
        HabitDTO habitDTO = new HabitDTO(1, "Test Habit", "Description", "freq", false, LocalDate.now(), 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getInputStream()).thenReturn(new MockServletInputStream(objectMapper.writeValueAsBytes(habitDTO)));
        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        habitController.doPost(request, response);

        verify(habitService, times(1)).createHabit(habitDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_InvalidRequest() throws Exception {
        HabitDTO habitDTO = new HabitDTO(1, "Test Habit", "Description", "freq", false, LocalDate.now(), 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getInputStream()).thenReturn(new MockServletInputStream(objectMapper.writeValueAsBytes(habitDTO)));
        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        habitController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertTrue(responseWriter.toString().contains("Validation failed"));
    }

    @Test
    void testDoGet_HabitByTitle() throws Exception {
        String title = "Test Habit";
        HabitDTO habitDTO = new HabitDTO(1, "Habit", "Description", "freq", false, LocalDate.now(), 1);


        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("title")).thenReturn(title);
        when(habitService.getHabitByTitle(title)).thenReturn(habitDTO);

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        habitController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(habitDTO), responseWriter.toString());
    }

    @Test
    void testDoGet_HabitsByUserId() throws Exception {
        Long userId = 1L;
        HabitDTO habitDTO = new HabitDTO(1, "Habit", "Description", "freq", false, LocalDate.now(), userId);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("user_id")).thenReturn(userId.toString());
        when(habitService.getAllHabits(userId)).thenReturn(Collections.singletonList(habitDTO));

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        habitController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(Collections.singletonList(habitDTO)), responseWriter.toString());
    }

    @Test
    void testDoDelete_ValidId() throws Exception {
        Long habitId = 1L;

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn(habitId.toString());

        habitController.doDelete(request, response);

        verify(habitService).deleteHabitById(habitId);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete_InvalidId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn(null);

        habitController.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}

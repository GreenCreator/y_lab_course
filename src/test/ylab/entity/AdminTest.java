package ylab.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ylab.entity.admin.Admin;
import ylab.entity.user.User;
import ylab.utils.UserManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminTest {
    private Admin admin;
    private UserManager userManager;
    private List<User> users;

    @BeforeEach
    void setUp() {
        userManager = mock(UserManager.class);

        users = new ArrayList<>();
        users.add(new User("User One", "user1@example.com", "password1", false));
        users.add(new User("User Two", "user2@example.com", "password2", false));

        admin = new Admin("Admin Name", "admin@example.com", "password", userManager);
    }

    @Test
    void testListAllUsers() {
        when(userManager.getAllUsers()).thenReturn(users);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        admin.listAllUsers();

        String expectedOutput = "ID: 1, Name: User One, Email: user1@example.com\n" + "ID: 2, Name: User Two, Email: user2@example.com";
        assertEquals(expectedOutput.trim(), outputStream.toString().trim());
    }

    @Test
    void testDeleteUser() {
        admin.deleteUser("user1@example.com");

        verify(userManager, times(1)).deleteUser("user1@example.com");
    }
}

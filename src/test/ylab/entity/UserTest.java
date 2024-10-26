package ylab.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ylab.entity.user.User;
import ylab.impl.HabitRepositoryImpl;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "password123", false, Mockito.mock(HabitRepositoryImpl.class));
    }

    @Test
    void testUserInitialization() {
        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
        assertFalse(user.GetBlockedStatus());
        assertNotNull(user.getHabitManager());
    }

    @Test
    void testSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    void testSetEmail() {
        user.setEmail("jane@example.com");
        assertEquals("jane@example.com", user.getEmail());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetBlockedStatus() {
        user.setBlockedStatus(true);
        assertTrue(user.GetBlockedStatus());

        user.setBlockedStatus(false);
        assertFalse(user.GetBlockedStatus());
    }

    @Test
    void testMultipleUsers() {
        User user2 = new User("Alice", "alice@example.com", "password456", true, Mockito.mock(HabitRepositoryImpl.class));
        assertEquals(2, user2.getId());  // Ensure id increments correctly
    }
}

package ylab.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ylab.entity.admin.Admin;
import ylab.entity.user.User;
import ylab.impl.HabitRepositoryImpl;
import ylab.impl.UserRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserManagerTest {

    private UserManager userManager;
    private User user;

    @BeforeEach
    public void setUp() {
        userManager = new UserManager(Mockito.mock(UserRepositoryImpl.class), Mockito.mock(HabitRepositoryImpl.class));
        userManager.registerUser("ivan", "test@example.com", "password");
        user = userManager.getUserByEmail("test@example.com");
    }

    @Test
    public void shouldUserLoginSuccessfully() {
        var loginUser = userManager.login("test@example.com", "password");
        assertThat(loginUser == user).isTrue();
    }

    @Test
    public void shouldAddUserSuccessfully() {
        List<User> users = userManager.getAllUsers();
        assertThat(users).contains(user);
    }

    @Test
    public void shouldAuthenticateUserSuccessfully() {
        User user = userManager.login("test@example.com", "password");
        assertThat(user != null).isTrue();
    }

    @Test
    public void shouldNotAuthenticateWithWrongPassword() {
        User user = userManager.login("test@example.com", "wrongpassword");
        assertThat(user != null).isFalse();
    }


    @Test
    public void shouldBlockUserSuccessfully() {
        userManager.blockUser("test@example.com");
        assertThat(user.GetBlockedStatus()).isTrue();
    }

    @Test
    public void shouldDeleteUserSuccessfully() {
        userManager.deleteUser("test@example.com");
        List<User> users = userManager.getAllUsers();
        assertThat(users).doesNotContain(user);
    }

    @Test
    public void shouldCreateAdminSuccessfully() {
        userManager.registerAdmin("admin", "admin@example.com", "admin");
        var admin = (Admin) userManager.getUserByEmail("admin@example.com");
        assertThat(admin.isAdmin()).isTrue();
    }
}

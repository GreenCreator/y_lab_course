package ylab.entity.admin;

import ylab.entity.user.User;
import ylab.utils.UserManager;

public class Admin extends User {
    private UserManager userManager;

    public Admin(String name, String email, String password, UserManager userManager) {
        super(name, email, password, true);
        this.userManager = userManager;
    }

    public void listAllUsers() {
        userManager.getAllUsers().forEach(user -> {
            System.out.print("ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail() +'\n');
        });
    }

    public void deleteUser(String email) {
        userManager.deleteUser(email);
    }
}

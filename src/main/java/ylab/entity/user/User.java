package ylab.entity.user;

import ylab.impl.HabitRepositoryImpl;
import ylab.utils.HabitManager;

public class User {
    private static int idCounter = 0;
    private long id;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean blockedStatus;
    private HabitManager habitManager;

    public User(String name, String email, String password, boolean isAdmin, HabitRepositoryImpl habitRepository) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.habitManager = new HabitManager(habitRepository);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public HabitManager getHabitManager() {
        return habitManager;
    }

    public void setBlockedStatus(boolean value) {
        this.blockedStatus = value;
    }

    public boolean GetBlockedStatus() {
        return blockedStatus;
    }

    public void setId(long id) {
        this.id = id;
    }
}

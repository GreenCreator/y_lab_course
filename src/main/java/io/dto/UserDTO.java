package io.dto;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean isAdmin;
    private Boolean blockedStatus;

    public UserDTO(Long id, String name, String email, String password, Boolean isAdmin, Boolean blockedStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.blockedStatus = blockedStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
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

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public boolean getBlockedStatus() {
        return blockedStatus;
    }

    public void setBlockedStatus(Boolean blockedStatus) {
        this.blockedStatus = blockedStatus;
    }
}

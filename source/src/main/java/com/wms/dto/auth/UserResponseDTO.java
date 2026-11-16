package com.wms.dto.auth;

import com.wms.util.UserRole;

public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private UserRole role;
    private Boolean active;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String username, String fullName, UserRole role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.active = true;
    }

    public UserResponseDTO(Long id, String username, String fullName, String email, UserRole role, Boolean active) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

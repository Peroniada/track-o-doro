package com.sperek.pomodorotracker.domain.user;

import com.sperek.infrastructure.enums.Role;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;
    private byte[] salt;
    private UserRole userRole;
    private UUID userGoals;

    public User(UUID id, String email, String password, byte[] salt, UUID userGoalsId) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.salt = salt;
        this.userRole = UserRole.USER;
        this.userGoals = userGoalsId;
    }

    public User(UUID id, String email, String password, byte[] salt,
        Role userRole, UUID fkUserGoalsId) {
        String role = userRole.getLiteral();
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.userRole = UserRole.valueOf(role);
        this.userGoals = fkUserGoalsId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }

    public byte[] getSalt() {
        return salt;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UUID getUserGoals() {
        return userGoals;
    }
}

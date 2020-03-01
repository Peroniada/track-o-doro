package com.sperek.pomodorotracker.domain.user;

import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;
    private byte[] salt;
    private UserRole userRole;
    private UserGoals userGoals;

    public User(String email, String password, UUID id, byte[] salt, UUID userGoalsId) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.salt = salt;
        this.userRole = UserRole.USER;
        this.userGoals = new UserGoals(new DailyGoal(1), new WeeklyGoal(1), userGoalsId);
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

    public UserGoals getUserGoals() {
        return userGoals;
    }
}

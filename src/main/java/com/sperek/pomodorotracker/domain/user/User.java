package com.sperek.pomodorotracker.domain.user;

import com.sperek.pomodorotracker.domain.model.DailyGoal;
import com.sperek.pomodorotracker.domain.model.WeeklyGoal;
import java.util.UUID;

public class User {
    private UUID userId;
    private String userMail;
    private String password;
    private byte[] salt;
    private UserRole role;
    private UserGoals userGoals;

    public User(String userMail, String password, UUID userId, byte[] salt) {
        this.userMail = userMail;
        this.password = password;
        this.userId = userId;
        this.salt = salt;
        this.role = UserRole.USER;
        this.userGoals = new UserGoals(new DailyGoal(1), new WeeklyGoal(1));
    }

    public String getUserMail() {
        return userMail;
    }

    public String getPassword() {
        return password;
    }

    public UUID getUserId() {
        return userId;
    }

    public byte[] getSalt() {
        return salt;
    }

    public UserRole getRole() {
        return role;
    }

    public UserGoals getUserGoals() {
        return userGoals;
    }
}

package com.sperek.trackodoro.user;

import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import java.util.UUID;

public class User {

    private UUID userId;
    private String userMail;
    private String password;
    private byte[] salt;
    private UserGoals userGoals;

    public User(String userMail, String password, UUID userId, byte[] salt) {
        this.userMail = userMail;
        this.password = password;
        this.userId = userId;
        this.salt = salt;
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

    public UserGoals getUserGoals() {
        return userGoals;
    }
}
